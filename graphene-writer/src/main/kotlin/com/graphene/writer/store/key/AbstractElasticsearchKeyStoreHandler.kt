package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.KeyStoreHandler
import com.graphene.writer.store.key.model.ElasticsearchFactory
import com.graphene.writer.store.key.model.ElasticsearchKeyStoreHandlerProperty
import com.graphene.writer.store.key.model.GrapheneKeyMapper
import com.graphene.writer.util.NamedThreadFactory
import net.iponweb.disthene.reader.utils.DateTimeUtils
import org.apache.log4j.Logger
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.get.MultiGetRequestBuilder
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.xcontent.XContentBuilder
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

// TODO duplicated branch node removing logic
abstract class AbstractElasticsearchKeyStoreHandler(
  private val elasticsearchFactory: ElasticsearchFactory,
  private var property: ElasticsearchKeyStoreHandlerProperty
) : KeyStoreHandler, Runnable {

  private val logger = Logger.getLogger(SimpleKeyStoreHandler::class.java)

  private lateinit var client: TransportClient
  private lateinit var scheduler: ScheduledExecutorService
  private lateinit var request: MetricMultiGetRequestBuilder
  private lateinit var index: String
  private lateinit var type: String
  private lateinit var bulkProcessor: BulkProcessor
  private lateinit var grapheneKeyMapper: GrapheneKeyMapper

  private var lastFlushTimeSeconds = DateTimeUtils.currentTimeSeconds()
  private var batchSize: Int = 0
  private var flushInterval: Long = 0

  private val metrics = ConcurrentLinkedQueue<GrapheneMetric>()

  @PostConstruct
  fun init() {
    client = elasticsearchFactory.transportClient()

    bulkProcessor = elasticsearchFactory.bulkProcessor(client)
    grapheneKeyMapper = GrapheneKeyMapper()

    index = property.index
    type = property.type
    request = MetricMultiGetRequestBuilder(client, index, type)
    batchSize = property.bulk!!.actions
    flushInterval = property.bulk!!.interval

    scheduler = Executors.newSingleThreadScheduledExecutor(NamedThreadFactory(SimpleKeyStoreHandler::class.simpleName!!))
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

    if (batchSize <= request.size() || lastFlushTimeSeconds < DateTimeUtils.currentTimeSeconds() - flushInterval) {
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
        val graphiteKeySb = StringBuilder()

        for (depth in parts.indices) {
          if (graphiteKeySb.toString().isNotEmpty()) {
            graphiteKeySb.append(".")
          }
          graphiteKeySb.append(parts[depth])
          try {
            val graphiteKeyPart = graphiteKeySb.toString()
            bulkProcessor.add(IndexRequest(index, type, metric.getTenant() + "_" + graphiteKeyPart)
              .source(source(metric.getTenant(), graphiteKeyPart, depth, depth == parts.size - 1)))
          } catch (e: IOException) {
            logger.error(e)
          }
        }
      }
    }

    request = MetricMultiGetRequestBuilder(client, index, type)
    lastFlushTimeSeconds = DateTimeUtils.currentTimeSeconds()
  }

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

  abstract fun source(tenant: String, graphiteKeyPart: String, depth: Int, leaf: Boolean): XContentBuilder

}
