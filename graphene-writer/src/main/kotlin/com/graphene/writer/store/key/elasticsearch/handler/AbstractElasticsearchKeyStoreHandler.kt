package com.graphene.writer.store.key.elasticsearch.handler

import com.graphene.common.utils.DateTimeUtils
import com.graphene.reader.utils.Jsons
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.Source
import com.graphene.writer.store.KeyStoreHandler
import com.graphene.writer.store.KeyStoreHandlerProperty
import com.graphene.writer.store.key.KeyCache
import com.graphene.writer.store.key.TimeBasedLocalKeyCache
import com.graphene.writer.store.key.elasticsearch.ElasticsearchClient
import com.graphene.writer.store.key.elasticsearch.ElasticsearchClientFactory
import com.graphene.writer.store.key.elasticsearch.GrapheneIndexRequest
import com.graphene.writer.store.key.elasticsearch.MultiGetRequestContainer
import com.graphene.writer.store.key.elasticsearch.property.ElasticsearchKeyStoreHandlerProperty
import com.graphene.writer.util.NamedThreadFactory
import java.util.Objects
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.common.xcontent.XContentFactory

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
  private var batchSize: Int = 0
  private var flushInterval: Long = 0

  private val metrics = LinkedBlockingDeque<GrapheneMetric>()
  private val names = hashSetOf<String>()
  private val keyCache: KeyCache = TimeBasedLocalKeyCache(5) // Default 5 Min

  init {
    val property = Jsons.from(keyStoreHandlerProperty.handler["property"], ElasticsearchKeyStoreHandlerProperty::class.java)
    logger = LogManager.getLogger(this::class.java)

    index = property.index
    type = property.type
    tenant = keyStoreHandlerProperty.tenant
    templateIndexPattern = property.templateIndexPattern
    batchSize = property.bulk.actions
    flushInterval = property.bulk.interval

    elasticsearchClient = elasticsearchClient(keyStoreHandlerProperty, elasticsearchClientFactory, property)
    elasticsearchClient.createTemplateIfNotExists(templateIndexPattern, templateName(), templateSource())
    elasticsearchClient.createIndexIfNotExists(index, tenant, DateTimeUtils.currentTimeMillis(), DateTimeUtils.currentTimeMillis())
    if (this is TagBasedKeyStoreHandler) {
      elasticsearchClient.createNameIndexIfNotExists(NAMES_INDEX, tenant)
    }
    keyStoreScheduler = Executors.newSingleThreadScheduledExecutor(NamedThreadFactory(this::class.simpleName!!))
    keyStoreScheduler.scheduleWithFixedDelay(this, 3_000, 500, TimeUnit.MILLISECONDS)

    multiGetRequestContainer = MultiGetRequestContainer()
  }

  private fun elasticsearchClient(keyStoreHandlerProperty: KeyStoreHandlerProperty, elasticsearchClientFactory: ElasticsearchClientFactory, property: ElasticsearchKeyStoreHandlerProperty): ElasticsearchClient {
    return elasticsearchClientFactory.createElasticsearchClient(keyStoreHandlerProperty.rotation, listOf(property.cluster), property.port, property.protocol)
  }

  override fun handle(grapheneMetric: GrapheneMetric) {
    metrics.offer(grapheneMetric)
  }

  override fun run() {
    try {
      val metricsList = mutableListOf<GrapheneMetric>()
      metrics.drainTo(metricsList)
      for (metric in metricsList) {
        if (Objects.nonNull(metric) && isProcessable(metric)) {
          addToBatch(metric)
        }
      }

      if (batchSize <= multiGetRequestContainer.size() || lastFlushTimeMillis < DateTimeUtils.currentTimeMillis() - flushInterval) {
        if (this is TagBasedKeyStoreHandler) {
          flushMetricNames()
        }
        flush()
      }
    } catch (e: Exception) {
      logger.error("Encountered error in busy loop: ", e)
      Thread.sleep(1000)
    }
  }

  private fun addToBatch(metric: GrapheneMetric) {
    val index = elasticsearchClient.getIndexWithDate(index, tenant, metric.timestampMillis())
    if (keyCache.putIfAbsent("${index}_${metric.id}")) {
      multiGetRequestContainer.add(index, type, metric)
    }
    if (metric.source == Source.GRAPHITE_TAG && ! names.contains(metric.metricKey())) {
      multiGetRequestContainer.addName("${NAMES_INDEX}_$tenant", TYPE_NAME, metric)
      names.add(metric.metricKey())
    }
  }

  private fun flushMetricNames() {
    if (0 >= multiGetRequestContainer.namesMultiGetRequest.items.size) {
      return
    }

    val namesMultiGetResponse = elasticsearchClient.mget(multiGetRequestContainer.namesMultiGetRequest, RequestOptions.DEFAULT)
    val bulkRequest = mutableListOf<GrapheneIndexRequest>()

    for (response in namesMultiGetResponse.responses) {
      if (response in namesMultiGetResponse.responses) {
        if (response.isFailed) {
          logger.error("Names get response failed!")
          continue
        }

        if (response.response.isExists) {
          names.add(response.id)
          continue
        }
        val tmp = XContentFactory.jsonBuilder()
        tmp.startObject()
        bulkRequest.add(GrapheneIndexRequest(response.id, tmp.endObject(), 0))
      }

      logger.info("Flushed namesMultiGetRequests: ${multiGetRequestContainer.namesMultiGetRequest.items.size}.")

      if (bulkRequest.isNotEmpty()) {
        elasticsearchClient.bulkAsyncNames(NAMES_INDEX, type, tenant, bulkRequest, RequestOptions.DEFAULT)
      }
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

      if (bulkRequest.isNotEmpty()) {
        elasticsearchClient.bulkAsync(index, type, tenant, bulkRequest, RequestOptions.DEFAULT)
        logger.info("Requested to write ${bulkRequest.size} keys to ES.")
      }

      multiGetRequestContainer = MultiGetRequestContainer()
    }

    lastFlushTimeMillis = DateTimeUtils.currentTimeMillis()
  }

  override fun close() {
    keyStoreScheduler.shutdown()
    logger.info("Sleeping for 10 seconds to allow leftovers to be written")
    try {
      Thread.sleep(10000)
    } catch (ignored: InterruptedException) {
    }
  }

  abstract fun isProcessable(metric: GrapheneMetric): Boolean

  abstract fun mapToGrapheneIndexRequests(metric: GrapheneMetric?): List<GrapheneIndexRequest>

  abstract fun templateSource(): String

  abstract fun templateName(): String

  companion object {
    const val NAMES_INDEX = "metric-names"
    const val TYPE_NAME = "name"
  }
}
