package com.graphene.writer.config

import com.graphene.writer.store.data.CassandraDataStoreHandlerProperty
import com.graphene.writer.store.key.ElasticsearchKeyStoreProperty
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer")
class GrapheneWriterConfiguration(
  var cassandraDataStoreHandlerProperty: CassandraDataStoreHandlerProperty,
  var elasticsearchKeyStoreProperty: ElasticsearchKeyStoreProperty,
  var statsProperty: StatsProperty
) {

  override fun toString(): String {
    return "GrapheneWriterConfiguration{" +
      ", cassandraDataStoreHandlerProperty=" + cassandraDataStoreHandlerProperty +
      ", elasticsearchKeyStoreProperty=" + elasticsearchKeyStoreProperty +
      ", statsProperty=" + statsProperty +
      '}'.toString()
  }
}
