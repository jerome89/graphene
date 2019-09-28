package com.graphene.writer.store

import com.graphene.writer.config.IndexConfiguration
import com.graphene.writer.config.StoreConfiguration
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
    storeConfiguration: StoreConfiguration
  ): CassandraDataStoreHandler {

    return CassandraDataStoreHandler(
      carbonConfiguration,
      storeConfiguration,
      CassandraFactory()
    )
  }

  @Bean
  fun elasticsearchKeyStoreHandler(
    indexConfiguration: IndexConfiguration
  ): ElasticsearchKeyStoreHandler {

    return ElasticsearchKeyStoreHandler(
      ElasticsearchFactory(indexConfiguration)
    )
  }
}
