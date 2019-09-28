package com.graphene.writer.store.key

import com.graphene.writer.config.IndexConfiguration
import com.graphene.writer.input.GrapheneMetric
import org.apache.log4j.Logger
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.unit.TimeValue
import java.util.concurrent.ConcurrentLinkedQueue

class ElasticsearchFactory(
  private val indexConfiguration: IndexConfiguration
) {

  private val logger = Logger.getLogger(ElasticsearchFactory::class.java)

  fun transportClient(): TransportClient {
    val settings = ImmutableSettings.settingsBuilder()
      .put("cluster.name", indexConfiguration.name)
      .build()

    var client = TransportClient(settings)

    for (node in indexConfiguration.cluster) {
      client.addTransportAddress(InetSocketTransportAddress(node, indexConfiguration.port))
    }

    return client
  }

  fun indexThread(client: TransportClient, metrics: ConcurrentLinkedQueue<GrapheneMetric>): IndexThread {
    return IndexThread(
      "grapheneIndexThread",
      client,
      metrics,
      indexConfiguration.bulk!!.actions,
      indexConfiguration.bulk!!.interval,
      indexConfiguration.index!!,
      indexConfiguration.type!!,
      BulkProcessor.builder(
        client,
        object : BulkProcessor.Listener {
          override fun beforeBulk(executionId: Long, request: BulkRequest) {}

          override fun afterBulk(executionId: Long, request: BulkRequest, response: BulkResponse) {
            logger.debug("stored " + request.numberOfActions() + " metrics")
          }

          override fun afterBulk(executionId: Long, request: BulkRequest, failure: Throwable) {
            logger.error(failure)
          }
        })
        .setBulkActions(indexConfiguration.bulk!!.actions)
        .setFlushInterval(TimeValue.timeValueSeconds(indexConfiguration.bulk!!.interval))
        .setConcurrentRequests(1)
        .build()
    )
  }
}
