package com.graphene.writer.event

import com.graphene.writer.input.GrapheneMetric

class MetricStoreEvent(
  val grapheneMetric: GrapheneMetric
) : GrapheneWriterEvent {

  override fun getTenant(): String {
    return grapheneMetric.getTenant()
  }
}
