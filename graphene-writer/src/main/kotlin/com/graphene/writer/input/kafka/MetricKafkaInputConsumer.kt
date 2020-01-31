package com.graphene.writer.input.kafka

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.processor.GrapheneProcessor
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka", name = ["enabled"], havingValue = "true")
class MetricKafkaInputConsumer(
  private val grapheneProcessor: GrapheneProcessor
) {

  private val logger: Logger = LogManager.getLogger(MetricKafkaInputConsumer::class)

  @KafkaListener(topics = ["#{'\${graphene.writer.input.kafka.topics}'.split(',')}"], containerFactory = "kafkaListenerContainerFactory")
  fun handle(grapheneMetric: GrapheneMetric) {
    try {
      grapheneProcessor.process(grapheneMetric)
    } catch (e: Throwable) {
      logger.error("Cannot process metric: $grapheneMetric")
    }
  }
}
