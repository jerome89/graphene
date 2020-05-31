package com.graphene.writer.input.kafka

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.processor.GrapheneProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "graphene.writer.input.kafka.custom", name = ["enabled"], havingValue = "true")
class CustomInputKafka(
  grapheneProcessor: GrapheneProcessor
) : AbstractInputKafka(grapheneProcessor) {

  @KafkaListener(
    topics = ["#{'\${graphene.writer.input.kafka.custom.topics}'.split(',')}"],
    containerFactory = "kafkaListenerContainerFactory"
  )
  override fun handle(grapheneMetrics: List<GrapheneMetric>?) {
    doHandle(grapheneMetrics)
  }
}
