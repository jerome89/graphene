package com.graphene.writer.input.kafka.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties

open class InputKafkaProperty(
  var bootstrapServer: String = "localhost:9092",
  var autoOffsetReset: String = "latest",
  var consumerGroupId: String? = null,
  var pollIntervalMs: Int = 5000,
  var maxPollRecords: Int = 1000,
  var keyDeserializerClass: String? = null,
  var valueDeserializerClass: String? = null
)

@ConfigurationProperties(prefix = "graphene.writer.input.kafka.custom")
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka.custom", name = ["enabled"], havingValue = "true")
class CustomInputKafkaProperty : InputKafkaProperty()

@ConfigurationProperties(prefix = "graphene.writer.input.kafka.prometheus")
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka.prometheus", name = ["enabled"], havingValue = "true")
class PrometheusInputKafkaProperty : InputKafkaProperty()

@ConfigurationProperties(prefix = "graphene.writer.input.kafka.influx-db")
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka.influxDb", name = ["enabled"], havingValue = "true")
class InfluxDbInputKafkaProperty : InputKafkaProperty()
