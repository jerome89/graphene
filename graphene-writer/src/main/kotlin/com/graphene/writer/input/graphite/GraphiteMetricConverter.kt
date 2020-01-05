package com.graphene.writer.input.graphite

import com.graphene.reader.utils.MetricRule
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.MetricConverter
import com.graphene.writer.input.Source
import java.util.Collections
import java.util.TreeMap

class GraphiteMetricConverter : MetricConverter<GraphiteMetric>, GraphiteAware {

  override fun convert(metric: GraphiteMetric): List<GrapheneMetric> {
    val tags = convertTags(metric)
    val id = getGraphiteKey(tags)

    val grapheneMetric = GrapheneMetric(
      source = Source.GRAPHITE,
      id = id,
      meta = Collections.emptyMap(),
      tags = tags,
      value = metric.value,
      timestampSeconds = metric.timestamp
    )

    return listOf(grapheneMetric)
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
