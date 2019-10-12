package com.graphene.writer.store

import com.graphene.writer.store.key.ElasticsearchKeyStoreProperty
import com.graphene.writer.store.data.CassandraDataStoreHandlerProperty
import com.graphene.writer.store.data.CassandraDataStoreHandler
import com.graphene.writer.store.data.CassandraFactory
import com.graphene.writer.store.key.ElasticsearchFactory
import com.graphene.writer.input.graphite.property.CarbonProperty
import com.graphene.writer.store.key.SimpleKeyStoreHandler
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

  @Bean
  fun elasticsearchKeyStoreHandler(
    property: ElasticsearchKeyStoreProperty
  ): StoreHandler {

    return SimpleKeyStoreHandler(
      ElasticsearchFactory(property),
      property
    )
  }
}
