package com.graphene.writer.input.kafka.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnProperty(prefix = "graphene.writer.input.kafka", name = ["enabled"], havingValue = "true")
@ConfigurationProperties(prefix = "graphene.writer.input.kafka")
class InputKafkaProperty(
  var bootstrapServer: String = "localhost:9092",
  var autoOffsetReset: String = "latest",
  var consumerGroupId: String? = null,
  var pollIntervalMs: Int = 5000,
  var maxPollRecords: Int = 1000,
  var keyDeserializerClass: String? = null,
  var valueDeserializerClass: String? = null
)
