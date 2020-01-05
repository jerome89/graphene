package com.graphene.writer.input.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.graphene.writer.input.graphite.GraphiteMetric
import com.graphene.writer.input.graphite.GraphiteMetricConverter
import com.graphene.writer.processor.GrapheneProcessor
import javax.annotation.PostConstruct
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka", name = ["enabled"], havingValue = "true")
class MetricKafkaInputConsumer(private val objectMapper: ObjectMapper, private val grapheneProcessor: GrapheneProcessor) {
  private val logger: Logger = LogManager.getLogger(MetricKafkaInputConsumer::class)
  private lateinit var graphiteMetricConverter: GraphiteMetricConverter

  @PostConstruct
  fun init() {
    graphiteMetricConverter = GraphiteMetricConverter()
  }

  @KafkaListener(topics = ["#{'\${graphene.writer.input.kafka.topics}'.split(',')}"], containerFactory = "kafkaListenerContainerFactory")
  fun consume(rawMetric: String) {
    try {
      val testRaw = "{key:a.b.c, value:350, timestamp:384}"
      transformAndWrite(testRaw)
    } catch (e: Exception) {
      logger.debug("Cannot processed message: $rawMetric")
    }
  }

  @Async("metricKafkaInputTransformExecutor")
  fun transformAndWrite(rawMetric: String) {
    val graphiteMetric: GraphiteMetric = objectMapper.readValue(rawMetric, GraphiteMetric::class.java)
    grapheneProcessor.process(graphiteMetricConverter.convert(graphiteMetric)[0])
  }
}
