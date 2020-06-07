package com.graphene.writer.input.kafka

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.processor.GrapheneProcessor
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.Payload

abstract class AbstractInputKafka(
  private val grapheneProcessor: GrapheneProcessor
) {

  private val log: Logger = LogManager.getLogger(javaClass)

  protected fun doHandle(grapheneMetrics: List<GrapheneMetric>?) {
    if (grapheneMetrics.isNullOrEmpty()) return

    try {
      for (grapheneMetric in grapheneMetrics) {
        grapheneProcessor.process(grapheneMetric)
      }
    } catch (e: Throwable) {
      log.error("Cannot process metric: $grapheneMetrics")
    }
  }

  abstract fun handle(@Payload(required = false) grapheneMetrics: List<GrapheneMetric>?)
}
