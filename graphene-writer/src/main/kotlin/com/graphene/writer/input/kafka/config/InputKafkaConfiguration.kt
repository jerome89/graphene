package com.graphene.writer.input.kafka.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.kafka.ConsumerFactoryUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory

@Configuration
class InputKafkaConfiguration(
  val customInputKafkaProperty: CustomInputKafkaProperty,
  val prometheusInputKafkaProperty: PrometheusInputKafkaProperty,
  val influxDbInputKafkaProperty: InfluxDbInputKafkaProperty
) {

  @Bean
  fun objectMapper(): ObjectMapper {
    return ObjectMapper().also {
      it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      it.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
      it.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
      it.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
    }
  }

  @Bean
  @ConditionalOnProperty(prefix = "graphene.writer.input.kafka.custom", name = ["enabled"], havingValue = "true")
  fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, GrapheneMetric> {
    return ConcurrentKafkaListenerContainerFactory<String, GrapheneMetric>().also {
      it.consumerFactory = ConsumerFactoryUtils.create(customInputKafkaProperty)
      it.containerProperties.idleBetweenPolls = customInputKafkaProperty.pollIntervalMs.toLong()
    }
  }

  @Bean
  @ConditionalOnProperty(prefix = "graphene.writer.input.kafka.prometheus", name = ["enabled"], havingValue = "true")
  fun prometheusKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, GrapheneMetric> {
    return ConcurrentKafkaListenerContainerFactory<String, GrapheneMetric>().also {
      it.consumerFactory = ConsumerFactoryUtils.create(prometheusInputKafkaProperty)
      it.containerProperties.idleBetweenPolls = prometheusInputKafkaProperty.pollIntervalMs.toLong()
    }
  }

  @Bean
  @ConditionalOnProperty(prefix = "graphene.writer.input.kafka.influx-db", name = ["enabled"], havingValue = "true")
  fun influxDbKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, GrapheneMetric> {
    return ConcurrentKafkaListenerContainerFactory<String, GrapheneMetric>().also {
      it.consumerFactory = ConsumerFactoryUtils.create(influxDbInputKafkaProperty)
      it.containerProperties.idleBetweenPolls = influxDbInputKafkaProperty.pollIntervalMs.toLong()
    }
  }
}
