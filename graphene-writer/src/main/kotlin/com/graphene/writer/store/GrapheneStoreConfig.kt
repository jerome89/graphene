package com.graphene.writer.store

import com.graphene.writer.input.graphite.property.CarbonProperty
import com.graphene.writer.store.data.CassandraDataStoreHandler
import com.graphene.writer.store.data.CassandraDataStoreHandlerProperty
import com.graphene.writer.store.data.CassandraFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrapheneStoreConfig {

  @Bean
  fun cassandraDataStoreHandler(
    carbonProperty: CarbonProperty,
    handlerProperty: CassandraDataStoreHandlerProperty
  ): StoreHandler {

    return CassandraDataStoreHandler(
      carbonProperty,
      handlerProperty,
      CassandraFactory()
    )
  }
}
