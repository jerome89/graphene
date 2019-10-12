package com.graphene.writer.store.key

import org.apache.log4j.Logger
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.unit.TimeValue

class ElasticsearchFactory(
  private val properties: ElasticsearchKeyStoreProperties
) {

  private val logger = Logger.getLogger(ElasticsearchFactory::class.java)

  fun transportClient(): TransportClient {
    val settings = ImmutableSettings.settingsBuilder()
      .put("cluster.name", properties.clusterName)
      .build()

    var client = TransportClient(settings)

    for (node in properties.cluster) {
      client.addTransportAddress(InetSocketTransportAddress(node, properties.port))
    }

    return client
  }

  fun bulkProcessor(client: TransportClient): BulkProcessor {
    return BulkProcessor.builder(
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
      .setBulkActions(properties.bulk!!.actions)
      .setFlushInterval(TimeValue.timeValueSeconds(properties.bulk!!.interval))
      .setConcurrentRequests(1)
      .build()
  }
}
