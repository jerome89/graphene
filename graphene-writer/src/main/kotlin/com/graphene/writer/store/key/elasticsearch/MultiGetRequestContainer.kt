package com.graphene.writer.store.key.elasticsearch

import com.graphene.writer.input.GrapheneMetric
import java.util.Objects
import org.elasticsearch.action.get.MultiGetRequest

class MultiGetRequestContainer(
  val multiGetRequest: MultiGetRequest = MultiGetRequest(),
  val namesMultiGetRequest: MultiGetRequest = MultiGetRequest(),
  val metrics: MutableMap<Index, GrapheneMetric> = mutableMapOf(),
  var from: Long? = null,
  var to: Long? = null
) {

  fun addName(index: String, type: String, metric: GrapheneMetric) {
    namesMultiGetRequest.add(MultiGetRequest.Item(index, type, metric.metricKey()))
  }

  fun add(index: String, type: String, metric: GrapheneMetric) {
    val timestampMillis = metric.timestampMillis()

    multiGetRequest.add(MultiGetRequest.Item(index, type, metric.id))
    metrics["${index}_${metric.id}"] = metric

    if (Objects.isNull(from) && Objects.isNull(to)) {
      from = timestampMillis
      to = timestampMillis
    }

    if (timestampMillis < from!!) {
      from = timestampMillis
    }

    if (to!! < timestampMillis) {
      to = timestampMillis
    }
  }

  fun size(): Int {
    return multiGetRequest.items.size
  }

  fun isMultiGetRequestsExist(): Boolean {
    return multiGetRequest.items.isNotEmpty()
  }

  fun multiGetRequestSize(): Int {
    return multiGetRequest.items.size
  }
}

typealias Index = String
