package com.graphene.writer.store.data

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "graphene.writer.store.data")
class StoreDataProperty(
  var cassafndraDataStoreHandlerProperty: CassandraDataStoreHandlerProperty
) {
  lateinit var rollup: String
  lateinit var retention: String
}
