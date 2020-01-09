package com.graphene.writer.input.kafka.config

import com.graphene.writer.input.kafka.deserializer.GraphiteDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnProperty(prefix = "graphene.writer.input.kafka", name = ["enabled"], havingValue = "true")
@ConfigurationProperties(prefix = "graphene.writer.input.kafka")
data class InputKafkaProperty(
  var bootstrapServer: String = "localhost:9092",
  var autoOffsetReset: String = "latest",
  var consumerGroupId: String? = null,
  var pollIntervalMs: Int = 5000,
  var maxPollRecords: Int = 1000,
  val keyDeserializerClass: String = StringDeserializer::class.qualifiedName!!,
  val valueDeserializerClass: String = GraphiteDeserializer::class.qualifiedName!!
)
