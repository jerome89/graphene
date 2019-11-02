package com.graphene.writer.event

import com.graphene.writer.input.GrapheneMetric
import org.springframework.context.ApplicationEvent

abstract class GrapheneWriterEvent(
  private val grapheneMetric: GrapheneMetric
) : ApplicationEvent(grapheneMetric) {

  fun getTenant(): String {
    return grapheneMetric.getTenant()
  }
}

data class GrapheneDataStoreEvent(val grapheneMetric: GrapheneMetric) : GrapheneWriterEvent(grapheneMetric)
