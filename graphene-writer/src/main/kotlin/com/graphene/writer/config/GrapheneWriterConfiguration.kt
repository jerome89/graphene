package com.graphene.writer.config

import com.graphene.writer.store.data.CassandraDataStoreHandlerProperty
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer")
class GrapheneWriterConfiguration(
  var cassandraDataStoreHandlerProperty: CassandraDataStoreHandlerProperty,
  var statsProperty: StatsProperty
) {

  override fun toString(): String {
    return "GrapheneWriterConfiguration{" +
      ", cassandraDataStoreHandlerProperty=" + cassandraDataStoreHandlerProperty +
      ", statsProperty=" + statsProperty +
      '}'.toString()
  }
}
