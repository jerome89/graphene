package com.graphene.writer.input.kafka

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.kafka.config.InputKafkaProperty
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Deserializer
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

object ConsumerFactoryUtils {

  fun create(inputKafkaProperty: InputKafkaProperty): ConsumerFactory<String, GrapheneMetric> {
    return DefaultKafkaConsumerFactory(
      consumerConfigs(inputKafkaProperty),
      keyDeserializer(inputKafkaProperty.keyDeserializerClass!!),
      valueDeserializer(inputKafkaProperty.valueDeserializerClass!!)
    )
  }

  private fun consumerConfigs(inputKafkaProperty: InputKafkaProperty): Map<String, Any?> {
    return mutableMapOf(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to inputKafkaProperty.bootstrapServer,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to keyDeserializer(inputKafkaProperty.keyDeserializerClass!!),
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to valueDeserializer(inputKafkaProperty.valueDeserializerClass!!),
      ConsumerConfig.GROUP_ID_CONFIG to inputKafkaProperty.consumerGroupId,
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to inputKafkaProperty.autoOffsetReset,
      ConsumerConfig.MAX_POLL_RECORDS_CONFIG to inputKafkaProperty.maxPollRecords
    )
  }

  private fun keyDeserializer(deserializerClass: String): Deserializer<String> {
    return Class.forName(deserializerClass).constructors.first().newInstance() as Deserializer<String>
  }

  private fun valueDeserializer(deserializerClass: String): Deserializer<GrapheneMetric> {
    return Class.forName(deserializerClass).constructors.first().newInstance() as Deserializer<GrapheneMetric>
  }

}
