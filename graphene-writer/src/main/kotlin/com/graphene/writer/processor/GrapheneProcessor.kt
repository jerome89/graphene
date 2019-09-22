package com.graphene.writer.processor

import com.graphene.writer.input.GrapheneMetric
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class GrapheneProcessor {

  // TODO assign executor
  @Async
  fun process(grapheneMetric: GrapheneMetric) {

  }
}
