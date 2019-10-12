package com.graphene.writer.store

import com.graphene.writer.store.key.ElasticsearchKeyStoreProperties
import com.graphene.writer.store.data.CassandraDataStoreProperties
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
    properties: CassandraDataStoreProperties
  ): StoreHandler {

    return CassandraDataStoreHandler(
      carbonProperty,
      properties,
      CassandraFactory()
    )
  }

  @Bean
  fun elasticsearchKeyStoreHandler(
    properties: ElasticsearchKeyStoreProperties
  ): StoreHandler {

    return SimpleKeyStoreHandler(
      ElasticsearchFactory(properties),
      properties
    )
  }
}
