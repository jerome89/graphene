package com.graphene.writer.store.key

import com.graphene.writer.config.ElasticsearchKeyStoreConfiguration
import org.apache.log4j.Logger
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.unit.TimeValue

class ElasticsearchFactory(
  val elasticsearchKeyStoreConfiguration: ElasticsearchKeyStoreConfiguration
) {

  private val logger = Logger.getLogger(ElasticsearchFactory::class.java)

  fun transportClient(): TransportClient {
    val settings = ImmutableSettings.settingsBuilder()
      .put("cluster.name", elasticsearchKeyStoreConfiguration.clusterName)
      .build()

    var client = TransportClient(settings)

    for (node in elasticsearchKeyStoreConfiguration.cluster) {
      client.addTransportAddress(InetSocketTransportAddress(node, elasticsearchKeyStoreConfiguration.port))
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
      .setBulkActions(elasticsearchKeyStoreConfiguration.bulk!!.actions)
      .setFlushInterval(TimeValue.timeValueSeconds(elasticsearchKeyStoreConfiguration.bulk!!.interval))
      .setConcurrentRequests(1)
      .build()
  }
}
