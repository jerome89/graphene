package com.graphene.writer.input

import com.graphene.reader.utils.MetricRule
import java.util.TreeMap

/***
 *
 * @since 1.0.0
 * @author dark
 * @author jerome89
 */
data class GrapheneMetric(
  val source: Source,
  val id: String? = null,
  val meta: MutableMap<String, String>,
  var tags: TreeMap<String, String>,
  var nodes: TreeMap<String, String>,
  var value: Double,
  var timestampSeconds: Long
) {

  fun metricKey(): String {
    return id!!.split(";")[0]
  }

  fun getTenant(): String {
    return meta.getOrDefault(TENANT, MetricRule.defaultTenant())
  }

  fun timestampMillis(): Long {
    return timestampSeconds * 1_000
  }

  companion object {
    private val TENANT = "@tenant"
  }
}

enum class Source {

  GRAPHITE,

  GRAPHITE_TAG,

  INFLUXDB
}
