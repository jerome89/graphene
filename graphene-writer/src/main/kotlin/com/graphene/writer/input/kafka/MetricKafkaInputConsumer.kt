package com.graphene.writer.input.kafka

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.processor.GrapheneProcessor
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka", name = ["enabled"], havingValue = "true")
class MetricKafkaInputConsumer(
  private val grapheneProcessor: GrapheneProcessor
) {

  private val log: Logger = LogManager.getLogger(javaClass)

  @KafkaListener(topics = ["#{'\${graphene.writer.input.kafka.topics}'.split(',')}"], containerFactory = "kafkaListenerContainerFactory")
  fun handle(@Payload(required = false) grapheneMetrics: List<GrapheneMetric>?) {
    if (grapheneMetrics.isNullOrEmpty()) return

    try {
      for (grapheneMetric in grapheneMetrics) {
        grapheneProcessor.process(grapheneMetric)
      }
    } catch (e: Throwable) {
      log.error("Cannot process metric: $grapheneMetrics")
    }
  }
}
