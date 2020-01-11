package com.graphene.writer.store

import com.graphene.writer.input.graphite.property.InputGraphiteCarbonProperty
import com.graphene.writer.store.data.CassandraDataStoreHandler
import com.graphene.writer.store.data.CassandraDataStoreHandlerProperty
import com.graphene.writer.store.data.CassandraFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrapheneStoreConfig {

  @Bean
  fun cassandraDataStoreHandler(
    inputGraphiteCarbonProperty: InputGraphiteCarbonProperty,
    handlerProperty: CassandraDataStoreHandlerProperty
  ): StoreHandler {

    return CassandraDataStoreHandler(
      inputGraphiteCarbonProperty,
      handlerProperty,
      CassandraFactory()
    )
  }
}
