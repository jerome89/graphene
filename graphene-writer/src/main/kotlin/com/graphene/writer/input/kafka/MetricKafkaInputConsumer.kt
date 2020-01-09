package com.graphene.writer.input.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.graphene.writer.input.graphite.GraphiteMetric
import com.graphene.writer.input.graphite.GraphiteMetricConverter
import com.graphene.writer.processor.GrapheneProcessor
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka", name = ["enabled"], havingValue = "true")
class MetricKafkaInputConsumer(
  private val objectMapper: ObjectMapper,
  private val grapheneProcessor: GrapheneProcessor
) {

  private val logger: Logger = LogManager.getLogger(MetricKafkaInputConsumer::class)
  private val graphiteMetricConverter: GraphiteMetricConverter = GraphiteMetricConverter()

  @KafkaListener(topics = ["#{'\${graphene.writer.input.kafka.topics}'.split(',')}"], containerFactory = "kafkaInputContainerFactory")
  fun handle(rawMetric: String) {
    try {
      val testRaw = "{key:a.b.c, value:350, timestamp:384}"
      transformAndWrite(testRaw)
    } catch (e: Exception) {
      logger.error("Cannot process metric: $rawMetric")
    }
  }

  @Async("metricKafkaInputTransformExecutor")
  fun transformAndWrite(rawMetric: String) {
    val graphiteMetric: GraphiteMetric = objectMapper.readValue(rawMetric, GraphiteMetric::class.java)
    grapheneProcessor.process(graphiteMetricConverter.convert(graphiteMetric)[0])
  }
}
