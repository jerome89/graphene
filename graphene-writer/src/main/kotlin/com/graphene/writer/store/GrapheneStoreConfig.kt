package com.graphene.writer.store

import com.graphene.writer.store.key.ElasticsearchKeyStoreProperties
import com.graphene.writer.store.data.CassandraDataStoreProperties
import com.graphene.writer.store.data.CassandraDataStoreHandler
import com.graphene.writer.store.data.CassandraFactory
import com.graphene.writer.store.key.ElasticsearchFactory
import com.graphene.writer.config.CarbonConfiguration
import com.graphene.writer.store.key.ElasticsearchKeyStoreHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrapheneStoreConfig {

  @Bean
  fun cassandraDataStoreHandler(
    carbonConfiguration: CarbonConfiguration,
    properties: CassandraDataStoreProperties
  ): CassandraDataStoreHandler {

    return CassandraDataStoreHandler(
      carbonConfiguration,
      properties,
      CassandraFactory()
    )
  }

  @Bean
  fun elasticsearchKeyStoreHandler(
    properties: ElasticsearchKeyStoreProperties
  ): ElasticsearchKeyStoreHandler {

    return ElasticsearchKeyStoreHandler(
      ElasticsearchFactory(properties),
      properties
    )
  }
}
