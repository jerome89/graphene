package com.graphene.writer.config

import net.iponweb.disthene.service.aggregate.CarbonConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer")
class GrapheneWriterConfiguration(
  var carbon: CarbonConfiguration,
  var cassandraDataStore: CassandraDataStoreConfiguration,
  var elasticsearchKeyStore: ElasticsearchKeyStoreConfiguration,
  var stats: StatsConfiguration
) {

  override fun toString(): String {
    return "GrapheneWriterConfiguration{" +
      "carbon=" + carbon +
      ", cassandraDataStore=" + cassandraDataStore +
      ", elasticsearchKeyStore=" + elasticsearchKeyStore +
      ", stats=" + stats +
      '}'.toString()
  }
}
