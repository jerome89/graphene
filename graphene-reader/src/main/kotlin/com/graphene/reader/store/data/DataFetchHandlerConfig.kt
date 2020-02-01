package com.graphene.reader.store.data

import com.graphene.common.rule.GrapheneRules
import com.graphene.common.store.data.cassandra.CassandraFactory
import com.graphene.reader.service.metric.DataFetchHandler
import com.graphene.reader.store.data.cassandra.handler.OffsetBasedDataFetchHandler
import com.graphene.reader.store.data.cassandra.handler.SimpleDataFetchHandler
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext

@Configuration
class DataFetchHandlerConfig(
  var dataFetchHandlersProperty: DataFetchHandlersProperty,
  var applicationContext: GenericApplicationContext
) {

  fun simpleDataFetchHandlerProperty(): DataFetchHandlerProperty {
    return dataFetchHandlersProperty.handlers["simple-data-fetch-handler"]!!
  }

  @Bean
  @ConditionalOnProperty(prefix = "graphene.reader.store.data.handlers.simple-data-fetch-handler", value = ["enabled"], havingValue = "true")
  @ConfigurationProperties(value = "graphene.reader.store.data.handlers.simple-data-fetch-handler")
  fun simpleDataFetchHandler(): DataFetchHandler {
    return SimpleDataFetchHandler(
      applicationContext.getBean("cassandraFactory") as CassandraFactory,
      simpleDataFetchHandlerProperty()
    )
  }

  fun offsetBasedDataFetchHandlerProperty(): DataFetchHandlerProperty {
    return dataFetchHandlersProperty.handlers["offset-based-data-fetch-handler"]!!
  }

  @Bean
  @ConditionalOnProperty(prefix = "graphene.reader.store.data.handlers.offset-based-data-fetch-handler", value = ["enabled"], havingValue = "true")
  @ConfigurationProperties(value = "graphene.reader.store.data.handlers.offset-based-data-fetch-handler")
  fun offsetBasedDataFetchHandler(): DataFetchHandler {
    return OffsetBasedDataFetchHandler(
      applicationContext.getBean("cassandraFactory") as CassandraFactory,
      offsetBasedDataFetchHandlerProperty()
    )
  }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "graphene.reader.store.data")
data class DataFetchHandlersProperty(
  var handlers: Map<HandlerId, DataFetchHandlerProperty>
)

data class DataFetchHandlerProperty(
  var tenant: String = GrapheneRules.DEFAULT_TENANT,
  var keyspace: String,
  var columnFamily: String,
  var rollup: Int = 60,
  var maxPoints: Int = Int.MAX_VALUE,
  var bucketSize: Int,
  var handler: Map<String, Any> = mapOf()
)

typealias HandlerId = String
