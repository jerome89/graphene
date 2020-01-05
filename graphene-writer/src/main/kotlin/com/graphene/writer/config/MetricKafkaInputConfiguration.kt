package com.graphene.writer.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.concurrent.Executor
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@EnableKafka
@Configuration
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka", name = ["enabled"], havingValue = "true")
@ConfigurationProperties(prefix = "graphene.writer.input.kafka")
class MetricKafkaInputConfiguration {
  var bootstrapServer: String = "localhost:9092"
  var consumerGroupId: String? = null
  var pollIntervalMs = 5000
  var maxPollRecords = 1000

  @Bean
  fun objectMapper(): ObjectMapper {
    val mapper = ObjectMapper()
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
    mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
    mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
    return mapper
  }

  @Bean
  fun consumerConfigs(): Map<String, Any?> {
    val props: MutableMap<String, Any?> = HashMap()
    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServer
    props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    props[ConsumerConfig.GROUP_ID_CONFIG] = consumerGroupId
    props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
    props[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = maxPollRecords
    return props
  }

  @Bean
  fun consumerFactory(): ConsumerFactory<String, String> {
    return DefaultKafkaConsumerFactory(consumerConfigs(),
      StringDeserializer(),
      StringDeserializer())
  }

  @Bean
  fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
    val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
    factory.consumerFactory = consumerFactory()
    factory.containerProperties.idleBetweenPolls = pollIntervalMs.toLong()
    return factory
  }

  @Bean
  fun metricKafkaInputTransformExecutor(): Executor {
    val executor = ThreadPoolTaskExecutor()
    executor.corePoolSize = Runtime.getRuntime().availableProcessors() * 2
    executor.maxPoolSize = Runtime.getRuntime().availableProcessors() * 2
    executor.threadNamePrefix = "MetricKafkaInputTransformExecutor-"
    executor.setWaitForTasksToCompleteOnShutdown(true)
    executor.initialize()
    return executor
  }
}
