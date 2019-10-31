package com.graphene.writer.store.key.handler

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.KeyStoreHandler
import com.graphene.writer.store.key.ElasticsearchClient
import com.graphene.writer.store.key.ElasticsearchClientFactory
import com.graphene.writer.store.key.GrapheneIndexRequest
import com.graphene.writer.store.key.property.ElasticsearchKeyStoreHandlerProperty
import com.graphene.writer.store.key.property.RotationProperty
import com.graphene.writer.util.NamedThreadFactory
import net.iponweb.disthene.reader.utils.DateTimeUtils
import org.apache.log4j.Logger
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.client.RequestOptions
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

// TODO duplicated branch node removing logic
abstract class AbstractElasticsearchKeyStoreHandler(
  private val elasticsearchClientFactory: ElasticsearchClientFactory,
  private val rotationProperty: RotationProperty,
  private val property: ElasticsearchKeyStoreHandlerProperty
) : KeyStoreHandler, Runnable {

  private val logger = Logger.getLogger(AbstractElasticsearchKeyStoreHandler::class.java)

  private lateinit var elasticsearchClient: ElasticsearchClient
  private lateinit var keyStoreScheduler: ScheduledExecutorService
  private lateinit var multiGetRequestContainer: MultiGetRequestContainer
  private lateinit var index: String
  private lateinit var type: String
  private lateinit var templateIndexPattern: String
  private lateinit var tenant: String

  private var lastFlushTimeSeconds = DateTimeUtils.currentTimeSeconds()
  private var batchSize: Int = 0
  private var flushInterval: Long = 0

  private val metrics = ConcurrentLinkedQueue<GrapheneMetric>()

  @PostConstruct
  fun init() {
    index = property.index
    type = property.type
    tenant = property.tenant
    templateIndexPattern = property.templateIndexPattern
    multiGetRequestContainer = MultiGetRequestContainer()
    batchSize = property.bulk!!.actions
    flushInterval = property.bulk!!.interval

    elasticsearchClient = elasticsearchClientFactory.createIndexRollingEsClient(rotationProperty, property.cluster)
    elasticsearchClient.createTemplateIfNotExists(templateIndexPattern, templateName(), templateSource())
    elasticsearchClient.createIndexIfNotExists(index, tenant)

    // 프로퍼티에서 명시한 Index 중 가장 마지막 Offset 을 가져온다.
    val latestIndex = elasticsearchClient.getLatestIndex(index, tenant)
    logger.info("latestIndex : $latestIndex")
//    val aliasDate = indexRollingClient.getAliasDate(latestIndex)


    // 가장 마지막 Offset 의 Index 의 alias 를 확인한다.

    keyStoreScheduler = Executors.newSingleThreadScheduledExecutor(NamedThreadFactory(SimpleKeyStoreHandler::class.simpleName!!))
    keyStoreScheduler.scheduleWithFixedDelay(this, 3_000, 500, TimeUnit.MILLISECONDS)
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

      if (0 < multiGetRequestContainer.size()) {
        flush()
      }
    } catch (e: Exception) {
      logger.error("Encountered error in busy loop: ", e)
      Thread.sleep(1000)
    }
  }

  private fun addToBatch(metric: GrapheneMetric) {
    multiGetRequestContainer.add(type, metric)

    if (batchSize <= multiGetRequestContainer.size() || lastFlushTimeSeconds < DateTimeUtils.currentTimeSeconds() - flushInterval) {
      flush()
    }
  }

  private fun flush() {
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
    lastFlushTimeSeconds = DateTimeUtils.currentTimeSeconds()
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

  @PreDestroy
  fun shutdown() {
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
