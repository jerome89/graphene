package com.graphene.writer.store.key.handler

import com.graphene.common.utils.DateTimeUtils
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.KeyStoreHandler
import com.graphene.writer.store.key.ElasticsearchClient
import com.graphene.writer.store.key.ElasticsearchClientFactory
import com.graphene.writer.store.key.GrapheneIndexRequest
import com.graphene.writer.store.key.KeyStoreHandlerProperty
import com.graphene.writer.store.key.property.ElasticsearchKeyStoreHandlerProperty
import com.graphene.writer.util.NamedThreadFactory
import net.iponweb.disthene.reader.utils.Jsons
import org.apache.log4j.Logger
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.client.RequestOptions
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

abstract class AbstractElasticsearchKeyStoreHandler(
  elasticsearchClientFactory: ElasticsearchClientFactory,
  keyStoreHandlerProperty: KeyStoreHandlerProperty
) : KeyStoreHandler, Runnable {

  private val logger: Logger

  private var elasticsearchClient: ElasticsearchClient
  private var keyStoreScheduler: ScheduledExecutorService
  private var multiGetRequestContainer: MultiGetRequestContainer
  private var index: String
  private var type: String
  private var templateIndexPattern: String
  private var tenant: String

  private var lastFlushTimeMillis = DateTimeUtils.currentTimeMillis()
  private var lastCacheResetTimeMillis = DateTimeUtils.currentTimeMillis()
  private var batchSize: Int = 0
  private var flushInterval: Long = 0
  private var cacheRefreshInterval: Long = 300_000 // Default 5 Min

  private val metrics = LinkedBlockingDeque<GrapheneMetric>()

  init {
    val property = Jsons.from(keyStoreHandlerProperty.handler["property"], ElasticsearchKeyStoreHandlerProperty::class.java)
    logger = Logger.getLogger(this::class.java)

    index = property.index
    type = property.type
    tenant = keyStoreHandlerProperty.tenant
    templateIndexPattern = property.templateIndexPattern
    multiGetRequestContainer = MultiGetRequestContainer()
    batchSize = property.bulk!!.actions
    flushInterval = property.bulk!!.interval

    elasticsearchClient = elasticsearchClient(keyStoreHandlerProperty, elasticsearchClientFactory, property)
    elasticsearchClient.createTemplateIfNotExists(templateIndexPattern, templateName(), templateSource())
    elasticsearchClient.createIndexIfNotExists(index, tenant, DateTimeUtils.currentTimeMillis(), DateTimeUtils.currentTimeMillis())

    keyStoreScheduler = Executors.newSingleThreadScheduledExecutor(NamedThreadFactory(SimpleKeyStoreHandler::class.simpleName!!))
    keyStoreScheduler.scheduleWithFixedDelay(this, 3_000, 500, TimeUnit.MILLISECONDS)
  }

  private fun elasticsearchClient(keyStoreHandlerProperty: KeyStoreHandlerProperty, elasticsearchClientFactory: ElasticsearchClientFactory, property: ElasticsearchKeyStoreHandlerProperty): ElasticsearchClient {
    return elasticsearchClientFactory.createElasticsearchClient(keyStoreHandlerProperty.rotation, listOf(property.cluster))
  }

  override fun handle(grapheneMetric: GrapheneMetric) {
    metrics.offer(grapheneMetric)
  }

  override fun run() {
    try {
      tryClearCache()

      val metricsList = mutableListOf<GrapheneMetric>()
      metrics.drainTo(metricsList)
      for (metric in metricsList) {
        if (Objects.nonNull(metric)) {
          addToBatch(metric)
        }
      }

      if (batchSize <= multiGetRequestContainer.size() || lastFlushTimeMillis < DateTimeUtils.currentTimeMillis() - flushInterval) {
        flush()
      }
    } catch (e: Exception) {
      logger.error("Encountered error in busy loop: ", e)
      Thread.sleep(1000)
    }
  }

  private fun addToBatch(metric: GrapheneMetric) {
    multiGetRequestContainer.add(type, metric)
  }

  private fun tryClearCache() {
    if (lastCacheResetTimeMillis < DateTimeUtils.currentTimeMillis() - cacheRefreshInterval) {
      logger.info("Total ${multiGetRequestContainer.size()} cache entries are flushed.")
      multiGetRequestContainer.clearMetrics()
      lastCacheResetTimeMillis = DateTimeUtils.currentTimeMillis()
    }
  }

  private fun flush() {
    if (0 >= multiGetRequestContainer.size()) {
      return
    }

    elasticsearchClient.createIndexIfNotExists(index, tenant, multiGetRequestContainer.from, multiGetRequestContainer.to)

    if (multiGetRequestContainer.isMultiGetRequestsExist()) {
      val multiGetResponse = elasticsearchClient.mget(multiGetRequestContainer.multiGetRequest, RequestOptions.DEFAULT)
      val bulkRequest = mutableListOf<GrapheneIndexRequest>()

      for (response in multiGetResponse.responses) {
        if (response.isFailed) {
          logger.error("Fail to check duplicated index because ${response.failure.message}")
          continue
        }

        if (response.response.isExists) {
          continue
        }

        val metric = multiGetRequestContainer.metrics["${response.index}_${response.id}"]
        bulkRequest.addAll(mapToGrapheneIndexRequests(metric))
      }

      logger.info("Flushed multiGetRequests: ${multiGetRequestContainer.multiGetRequestSize()}.")
      multiGetRequestContainer.clearMultiGetRequest()

      if (bulkRequest.isEmpty()) {
        return
      }

      elasticsearchClient.bulkAsync(index, type, tenant, bulkRequest, RequestOptions.DEFAULT)

      multiGetRequestContainer = MultiGetRequestContainer()
      logger.info("Requested to write ${bulkRequest.size} keys to ES.")
    }

    lastFlushTimeMillis = DateTimeUtils.currentTimeMillis()
  }

  private inner class MultiGetRequestContainer(
    val multiGetRequest: MultiGetRequest = MultiGetRequest(),
    val metrics: MutableMap<Index, GrapheneMetric> = mutableMapOf(),
    var from: Long? = null,
    var to: Long? = null
  ) {

    fun clearMultiGetRequest() {
      multiGetRequest.items.clear()
    }

    fun clearMetrics() {
      metrics.clear()
    }

    fun add(type: String, metric: GrapheneMetric) {
      val timestampMillis = metric.timestampMillis()
      val indexWithDate = elasticsearchClient.getIndexWithDate(index, tenant, timestampMillis)

      if (!metrics.contains("${indexWithDate}_${metric.getId()}")) {
        metrics["${indexWithDate}_${metric.getId()}"] = metric
        multiGetRequest.add(MultiGetRequest.Item(indexWithDate, type, metric.getId()))

        if (Objects.isNull(from) && Objects.isNull(to)) {
          from = timestampMillis
          to = timestampMillis
        }

        if (timestampMillis < from!!) {
          from = timestampMillis
        }

        if (to!! < timestampMillis) {
          to = timestampMillis
        }
      }
    }

    fun size(): Int {
      return metrics.size
    }

    fun isMultiGetRequestsExist(): Boolean {
      return multiGetRequest.items.isNotEmpty()
    }

    fun multiGetRequestSize(): Int {
      return multiGetRequest.items.size
    }
  }

  override fun close() {
    keyStoreScheduler.shutdown()
    logger.info("Sleeping for 10 seconds to allow leftovers to be written")
    try {
      Thread.sleep(10000)
    } catch (ignored: InterruptedException) {
    }
  }

  abstract fun mapToGrapheneIndexRequests(metric: GrapheneMetric?): List<GrapheneIndexRequest>

  abstract fun templateSource(): String

  abstract fun templateName(): String
}

typealias Index = String
