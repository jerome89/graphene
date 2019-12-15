package com.graphene.writer.input.graphite

import com.graphene.reader.utils.MetricRule
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.MetricConverter
import java.util.Collections
import java.util.TreeMap

class GraphiteMetricConverter : MetricConverter<GraphiteMetric> {

  override fun convert(metric: GraphiteMetric): GrapheneMetric {
    val grapheneMetric = GrapheneMetric(
      Collections.emptyMap(),
      convertTags(metric),
      mutableMapOf(),
      metric.timestamp
    )

    grapheneMetric.metrics[grapheneMetric.getGraphiteKey()] = metric.value
    return grapheneMetric
  }

  private fun convertTags(metric: GraphiteMetric): TreeMap<String, String> {
    val separatedMetricKey = metric.key.split(DELIMITER)

    val tags = TreeMap<String, String>()
    for ((index, key) in separatedMetricKey.withIndex()) {
      tags["$index"] = MetricRule.generate(key)
    }
    return tags
  }

  companion object {
    private const val DELIMITER = "."
  }
}
