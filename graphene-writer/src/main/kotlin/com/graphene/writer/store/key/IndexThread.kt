package com.graphene.writer.store.key

import com.graphene.writer.config.ElasticsearchKeyStoreConfiguration
import com.graphene.writer.input.GrapheneMetric
import org.apache.log4j.Logger
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.get.MultiGetRequestBuilder
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.xcontent.XContentFactory

import java.io.IOException
import java.util.HashMap
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author Andrei Ivanov
 */
class IndexThread(
  name: String,
  private val client: TransportClient,
  metrics: ConcurrentLinkedQueue<GrapheneMetric>,
  private val elasticsearchKeyStoreConfiguration: ElasticsearchKeyStoreConfiguration,
  private val bulkProcessor: BulkProcessor
) : Thread(name) {

  private val logger = Logger.getLogger(IndexThread::class.java)

  @Volatile
  protected var shutdown = false
  protected var metrics: Queue<GrapheneMetric>

  private var lastFlushTimestamp = System.currentTimeMillis() / 1000L
  private var request: MetricMultiGetRequestBuilder? = null
  private var index: String
  private var type: String
  private var batchSize: Int
  private var flushInterval: Long = 0

  init {
    this.metrics = metrics
    this.index = elasticsearchKeyStoreConfiguration.index!!
    this.type = elasticsearchKeyStoreConfiguration.type!!
    this.request = MetricMultiGetRequestBuilder(client, index, type)
    this.batchSize = elasticsearchKeyStoreConfiguration.bulk!!.actions
    this.flushInterval = elasticsearchKeyStoreConfiguration.bulk!!.interval
  }

  override fun run() {
    while (!shutdown) {
      try {
        val metric = metrics.poll()
        if (metric != null) {
          addToBatch(metric)
        } else {
          Thread.sleep(100)
        }
      } catch (e: Exception) {
        logger.error("Encountered error in busy loop: ", e)
      }

    }

    if (request!!.size() > 0) {
      flush()
    }
  }

  private fun addToBatch(metric: GrapheneMetric) {
    request!!.add(metric)

    if (request!!.size() >= batchSize || lastFlushTimestamp < System.currentTimeMillis() / 1000L - flushInterval) {
      flush()
      lastFlushTimestamp = System.currentTimeMillis() / 1000L
    }
  }

  private fun flush() {
    val multiGetItemResponse = request!!.execute().actionGet()

    for (response in multiGetItemResponse.responses) {
      if (response.isFailed) {
        logger.error("Get failed: " + response.failure.message)
      }

      val metric = request!!.metrics[response.id]
      if (response.isFailed || !response.response.isExists) {
        val parts = metric!!.getGraphiteKey()!!.split("\\.".toRegex())
        val sb = StringBuilder()

        for (i in parts.indices) {
          if (sb.toString().isNotEmpty()) {
            sb.append(".")
          }
          sb.append(parts[i])
          try {
            bulkProcessor.add(IndexRequest(index, type, metric.getTenant() + "_" + sb.toString()).source(
              XContentFactory.jsonBuilder().startObject()
                .field("tenant", metric.getTenant())
                .field("path", sb.toString())
                .field("depth", i + 1)
                .field("leaf", i == parts.size - 1)
                .endObject()
            ))
          } catch (e: IOException) {
            logger.error(e)
          }

        }

      }

    }

    request = MetricMultiGetRequestBuilder(client, index, type)
  }

  fun shutdown() {
    shutdown = true
  }

  private inner class MetricMultiGetRequestBuilder(client: Client, private val index: String, private val type: String) : MultiGetRequestBuilder(client) {
    internal var metrics: MutableMap<String, GrapheneMetric> = HashMap()

    fun add(metric: GrapheneMetric): MultiGetRequestBuilder {
      metrics[metric.getId()] = metric
      return super.add(index, type, metric.getId())
    }

    fun size(): Int {
      return metrics.size
    }
  }
}
