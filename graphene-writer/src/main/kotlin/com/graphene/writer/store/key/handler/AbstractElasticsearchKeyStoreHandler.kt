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
import java.util.Objects
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import net.iponweb.disthene.reader.utils.Jsons
import org.apache.log4j.Logger
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.client.RequestOptions

abstract class AbstractElasticsearchKeyStoreHandler(
  elasticsearchClientFactory: ElasticsearchClientFactory,
  keyStoreHandlerProperty: KeyStoreHandlerProperty
) : KeyStoreHandler, Runnable {

  private val logger = Logger.getLogger(AbstractElasticsearchKeyStoreHandler::class.java)

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

  private val metrics = ConcurrentLinkedQueue<GrapheneMetric>()

  init {
    val property = Jsons.from(keyStoreHandlerProperty.handler["property"], ElasticsearchKeyStoreHandlerProperty::class.java)

    index = property.index
    type = property.type
    tenant = keyStoreHandlerProperty.tenant
    templateIndexPattern = property.templateIndexPattern
    multiGetRequestContainer = MultiGetRequestContainer()
    batchSize = property.bulk!!.actions
    flushInterval = property.bulk!!.interval

    elasticsearchClient = elasticsearchClient(keyStoreHandlerProperty, elasticsearchClientFactory, property)
    elasticsearchClient.createTemplateIfNotExists(templateIndexPattern, templateName(), templateSource())
    elasticsearchClient.createIndexIfNotExists(index, tenant)

    keyStoreScheduler = Executors.newSingleThreadScheduledExecutor(NamedThreadFactory(SimpleKeyStoreHandler::class.simpleName!!))
    keyStoreScheduler.scheduleWithFixedDelay(this, 3_000, 500, TimeUnit.MILLISECONDS)
  }

  private fun elasticsearchClient(keyStoreHandlerProperty: KeyStoreHandlerProperty, elasticsearchClientFactory: ElasticsearchClientFactory, property: ElasticsearchKeyStoreHandlerProperty): ElasticsearchClient {
    return if (keyStoreHandlerProperty.rotation.disableRotation()) {
      elasticsearchClientFactory.createElasticsearchClient(listOf(property.cluster))
    } else {
      elasticsearchClientFactory.createIndexRollingEsClient(keyStoreHandlerProperty.rotation, listOf(property.cluster))
    }
  }

  override fun handle(grapheneMetric: GrapheneMetric) {
    metrics.offer(grapheneMetric)
  }

  override fun run() {
    try {
      val metric = metrics.poll()
      if (Objects.nonNull(metric)) {
        addToBatch(metric)
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

  private fun flush() {
    if (0 >= multiGetRequestContainer.size()) {
      return
    }

    elasticsearchClient.createIndexIfNotExists(index, tenant)

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

      val metric = multiGetRequestContainer.metrics[response.id]
      bulkRequest.addAll(mapToGrapheneIndexRequests(metric))
    }

    if (bulkRequest.isEmpty()) {
      return
    }

    val bulkResponse = elasticsearchClient.bulk(index, type, tenant, bulkRequest, RequestOptions.DEFAULT)
    if (bulkResponse.hasFailures()) {
      logger.error("Fail to index metric key, reason : ${bulkResponse.buildFailureMessage()}")
    }
    multiGetRequestContainer = MultiGetRequestContainer()
    lastFlushTimeMillis = DateTimeUtils.currentTimeMillis()
  }

  private inner class MultiGetRequestContainer(
    val multiGetRequest: MultiGetRequest = MultiGetRequest(),
    val metrics: MutableMap<String, GrapheneMetric> = mutableMapOf()
  ) {

    fun add(type: String, metric: GrapheneMetric) {
      metrics[metric.getId()] = metric
      multiGetRequest.add(MultiGetRequest.Item(elasticsearchClient.getIndexWithDate(index, tenant), type, metric.getId()))
    }

    fun size(): Int {
      return metrics.size
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
