package com.graphene.writer.store

import com.graphene.writer.config.ElasticsearchKeyStoreConfiguration
import com.graphene.writer.config.CassandraDataStoreConfiguration
import com.graphene.writer.store.data.CassandraDataStoreHandler
import com.graphene.writer.store.data.CassandraFactory
import com.graphene.writer.store.key.ElasticsearchFactory
import net.iponweb.disthene.service.aggregate.CarbonConfiguration
import com.graphene.writer.store.key.ElasticsearchKeyStoreHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrapheneStoreConfig {

  @Bean
  fun cassandraDataStoreHandler(
    carbonConfiguration: CarbonConfiguration,
    cassandraDataStoreConfiguration: CassandraDataStoreConfiguration
  ): CassandraDataStoreHandler {

    return CassandraDataStoreHandler(
      carbonConfiguration,
      cassandraDataStoreConfiguration,
      CassandraFactory()
    )
  }

  @Bean
  fun elasticsearchKeyStoreHandler(
    elasticsearchKeyStoreConfiguration: ElasticsearchKeyStoreConfiguration
  ): ElasticsearchKeyStoreHandler {

    return ElasticsearchKeyStoreHandler(
      ElasticsearchFactory(elasticsearchKeyStoreConfiguration)
    )
  }
}
