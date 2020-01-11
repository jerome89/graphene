package com.graphene.writer.input.kafka.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.graphene.writer.input.GrapheneMetric
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Deserializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@EnableKafka
@Configuration
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka", name = ["enabled"], havingValue = "true")
class MetricKafkaInputConfiguration(
  val inputKafkaProperty: InputKafkaProperty
) {

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
    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = inputKafkaProperty.bootstrapServer
    props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = keyDeserializer(inputKafkaProperty.keyDeserializerClass)
    props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = valueDeserializer(inputKafkaProperty.valueDeserializerClass)
    props[ConsumerConfig.GROUP_ID_CONFIG] = inputKafkaProperty.consumerGroupId
    props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = inputKafkaProperty.autoOffsetReset
    props[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = inputKafkaProperty.maxPollRecords
    return props
  }

  @Bean
  fun consumerFactory(): ConsumerFactory<String, GrapheneMetric> {
    return DefaultKafkaConsumerFactory(consumerConfigs(),
      keyDeserializer(inputKafkaProperty.keyDeserializerClass),
      valueDeserializer(inputKafkaProperty.valueDeserializerClass))
  }

  @Bean
  fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, GrapheneMetric> {
    val factory = ConcurrentKafkaListenerContainerFactory<String, GrapheneMetric>()
    factory.consumerFactory = consumerFactory()
    factory.containerProperties.idleBetweenPolls = inputKafkaProperty.pollIntervalMs.toLong()
    return factory
  }

  private fun keyDeserializer(deserializerClass: String): Deserializer<String> {
    return Class.forName(deserializerClass).constructors.first().newInstance() as Deserializer<String>
  }

  private fun valueDeserializer(deserializerClass: String): Deserializer<GrapheneMetric> {
    return Class.forName(deserializerClass).constructors.first().newInstance() as Deserializer<GrapheneMetric>
  }
}
