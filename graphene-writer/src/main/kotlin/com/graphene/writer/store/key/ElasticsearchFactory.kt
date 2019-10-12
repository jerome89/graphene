package com.graphene.writer.store.key

import com.graphene.writer.store.key.property.SimpleKeyStoreHandlerProperty
import org.apache.log4j.Logger
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.unit.TimeValue
import org.springframework.stereotype.Component

@Component
class ElasticsearchFactory(
  private val property: SimpleKeyStoreHandlerProperty
) {

  private val logger = Logger.getLogger(ElasticsearchFactory::class.java)

  fun transportClient(): TransportClient {
    val settings = ImmutableSettings.settingsBuilder()
      .put("cluster.name", property.clusterName)
      .build()

    var client = TransportClient(settings)

    for (node in property.cluster) {
      client.addTransportAddress(InetSocketTransportAddress(node, property.port))
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
      .setBulkActions(property.bulk!!.actions)
      .setFlushInterval(TimeValue.timeValueSeconds(property.bulk!!.interval))
      .setConcurrentRequests(1)
      .build()
  }
}
