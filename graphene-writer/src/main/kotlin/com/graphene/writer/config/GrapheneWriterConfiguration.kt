package com.graphene.writer.config

import com.graphene.writer.store.data.CassandraDataStoreProperties
import com.graphene.writer.store.key.ElasticsearchKeyStoreProperties
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer")
class GrapheneWriterConfiguration(
  var cassandraDataStore: CassandraDataStoreProperties,
  var elasticsearchKeyStore: ElasticsearchKeyStoreProperties,
  var stats: StatsConfiguration
) {

  override fun toString(): String {
    return "GrapheneWriterConfiguration{" +
      ", cassandraDataStore=" + cassandraDataStore +
      ", elasticsearchKeyStore=" + elasticsearchKeyStore +
      ", stats=" + stats +
      '}'.toString()
  }
}
