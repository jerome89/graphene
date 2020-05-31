package com.graphene.writer.input.kafka

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.processor.GrapheneProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka.prometheus", name = ["enabled"], havingValue = "true")
class PrometheusInputKafka(
  grapheneProcessor: GrapheneProcessor
): AbstractInputKafka(grapheneProcessor) {

  @KafkaListener(
    topics = ["#{'\${graphene.writer.input.kafka.prometheus.topics}'.split(',')}"],
    containerFactory = "prometheusKafkaListenerContainerFactory"
  )
  override fun handle(@Payload(required = false) grapheneMetrics: List<GrapheneMetric>?) {
    doHandle(grapheneMetrics)
  }
}
