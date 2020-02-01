package com.graphene.reader.store.key

import com.graphene.reader.service.index.KeySearchHandler
import com.graphene.reader.store.key.elasticsearch.handler.ElasticsearchClient
import com.graphene.reader.store.key.elasticsearch.handler.IndexBasedKeySearchHandler
import com.graphene.reader.store.key.elasticsearch.handler.SimpleKeySearchHandler
import com.graphene.reader.store.key.elasticsearch.handler.TagBasedKeySearchHandler
import com.graphene.reader.store.key.elasticsearch.optimizer.ElasticsearchQueryOptimizer
import com.graphene.reader.store.key.elasticsearch.property.IndexBasedKeySearchHandlerProperty
import com.graphene.reader.store.key.elasticsearch.property.IndexProperty
import com.graphene.reader.store.key.elasticsearch.property.SimpleKeySearchHandlerProperty
import com.graphene.reader.store.key.elasticsearch.property.TagBasedKeySearchHandlerProperty
import com.graphene.reader.store.tag.elasticsearch.optimizer.ElasticsearchTagSearchQueryOptimizer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeySearchHandlerConfig {

  @Bean
  @ConditionalOnProperty(prefix = "graphene.reader.store.key.handlers.index-based-key-search-handler", value = ["enabled"], havingValue = "true")
  @ConfigurationProperties(value = "graphene.reader.store.key.handlers.index-based-key-search-handler")
  fun indexBasedKeySearchHandlerProperty(): IndexProperty {
    return IndexBasedKeySearchHandlerProperty()
  }

  @Bean
  @ConditionalOnProperty(prefix = "graphene.reader.store.key.handlers.index-based-key-search-handler", value = ["enabled"], havingValue = "true")
  fun indexBasedKeySearchHandler(elasticsearchClient: ElasticsearchClient, elasticsearchQueryOptimizer: ElasticsearchQueryOptimizer): KeySearchHandler {
    return IndexBasedKeySearchHandler(elasticsearchClient, elasticsearchQueryOptimizer)
  }

  @Bean
  @ConditionalOnProperty(prefix = "graphene.reader.store.key.handlers.simple-key-search-handler", value = ["enabled"], havingValue = "true")
  @ConfigurationProperties("graphene.reader.store.key.handlers.simple-key-search-handler")
  fun simpleKeySearchHandlerProperty(): IndexProperty {
    return SimpleKeySearchHandlerProperty()
  }

  @Bean
  @ConditionalOnProperty(prefix = "graphene.reader.store.key.handlers.simple-key-search-handler", value = ["enabled"], havingValue = "true")
  fun simpleKeySearchHandler(elasticsearchClient: ElasticsearchClient): KeySearchHandler {
    return SimpleKeySearchHandler(elasticsearchClient)
  }

  @Bean
  @ConditionalOnProperty(prefix = "graphene.reader.store.key.handlers.tag-based-key-search-handler", value = ["enabled"], havingValue = "true")
  @ConfigurationProperties(value = "graphene.reader.store.key.handlers.tag-based-key-search-handler")
  fun tagBasedKeySearchHandlerProperty(): IndexProperty {
    return TagBasedKeySearchHandlerProperty()
  }

  @Bean
  @ConditionalOnProperty(prefix = "graphene.reader.store.key.handlers.tag-based-key-search-handler", value = ["enabled"], havingValue = "true")
  fun tagBasedKeySearchHandler(elasticsearchClient: ElasticsearchClient, elasticsearchTagSearchQueryOptimizer: ElasticsearchTagSearchQueryOptimizer): KeySearchHandler {
    return TagBasedKeySearchHandler(elasticsearchClient, elasticsearchTagSearchQueryOptimizer)
  }
}
