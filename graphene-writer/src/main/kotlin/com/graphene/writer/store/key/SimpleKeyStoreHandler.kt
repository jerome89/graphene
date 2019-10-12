package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.StoreHandler
import com.graphene.writer.util.NamedThreadFactory
import org.apache.log4j.Logger
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.get.MultiGetRequestBuilder
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import java.io.IOException
import java.util.*

import javax.annotation.PreDestroy
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

/**
 * @author Andrei Ivanov
 * @author dark
 */
class SimpleKeyStoreHandler(
  private val elasticsearchFactory: ElasticsearchFactory,
  private val properties: ElasticsearchKeyStoreProperties
) : StoreHandler, Runnable {

  private val logger = Logger.getLogger(SimpleKeyStoreHandler::class.java)

  private lateinit var client: TransportClient
  private lateinit var scheduler: ScheduledExecutorService
  private lateinit var request: MetricMultiGetRequestBuilder
  private lateinit var index: String
  private lateinit var type: String
  private lateinit var bulkProcessor: BulkProcessor
  private lateinit var grapheneKeyMapper: GrapheneKeyMapper

  private var lastFlushTimeSeconds = currentTimeSeconds()
  private var batchSize: Int = 0
  private var flushInterval: Long = 0

  private val metrics = ConcurrentLinkedQueue<GrapheneMetric>()

  @PostConstruct
  fun init() {
    client = elasticsearchFactory.transportClient()

    bulkProcessor = elasticsearchFactory.bulkProcessor(client)
    grapheneKeyMapper = GrapheneKeyMapper()

    index = properties.index!!
    type = properties.type!!
    request = MetricMultiGetRequestBuilder(client, index, type)
    batchSize = properties.bulk!!.actions
    flushInterval = properties.bulk!!.interval

    scheduler = Executors.newSingleThreadScheduledExecutor(NamedThreadFactory("grapheneIndexThread"))
    scheduler.scheduleWithFixedDelay(this, 3000, 100, TimeUnit.MILLISECONDS)
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
    } catch (e: Exception) {
      logger.error("Encountered error in busy loop: ", e)
    }

    if (0 < request.size()) {
      flush()
    }
  }

  private fun addToBatch(metric: GrapheneMetric) {
    request.add(metric)

    if (batchSize <= request.size() || lastFlushTimeSeconds < currentTimeSeconds() - flushInterval) {
      flush()
    }
  }

  private fun flush() {
    val multiGetItemResponse = request.execute().actionGet()

    for (response in multiGetItemResponse.responses) {
      if (response.isFailed) {
        logger.error("Get failed: " + response.failure.message)
      }

      val metric = request.metrics[response.id]
      if (response.isFailed || !response.response.isExists) {
        val parts = metric!!.getGraphiteKeyParts()
        val sb = StringBuilder()

        for (i in parts.indices) {
          if (sb.toString().isNotEmpty()) {
            sb.append(".")
          }
          sb.append(parts[i])
          try {
            bulkProcessor.add(IndexRequest(index, type, metric.getTenant() + "_" + sb.toString())
              .source(grapheneKeyMapper.mapGrapheneMetricKey(metric, sb, i, parts)
              ))
          } catch (e: IOException) {
            logger.error(e)
          }
        }
      }
    }

    request = MetricMultiGetRequestBuilder(client, index, type)
    lastFlushTimeSeconds = currentTimeSeconds()
  }

  private fun currentTimeSeconds() = System.currentTimeMillis() / 1000L

  private inner class MetricMultiGetRequestBuilder(
    client: Client,
    private val index: String,
    private val type: String
  ) : MultiGetRequestBuilder(client) {

    internal var metrics: MutableMap<String, GrapheneMetric> = HashMap()

    fun add(metric: GrapheneMetric): MultiGetRequestBuilder {
      metrics[metric.getId()] = metric
      return super.add(index, type, metric.getId())
    }

    fun size(): Int {
      return metrics.size
    }
  }

  @PreDestroy
  fun shutdown() {
    scheduler.shutdown()
    logger.info("Sleeping for 10 seconds to allow leftovers to be written")
    try {
      Thread.sleep(10000)
    } catch (ignored: InterruptedException) {
    }

    logger.info("Closing ES client")
    client.close()
  }
}
