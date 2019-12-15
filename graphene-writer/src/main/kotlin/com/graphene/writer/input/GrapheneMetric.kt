package com.graphene.writer.input

import com.graphene.reader.utils.MetricRule
import com.graphene.writer.input.graphite.GraphiteAware
import java.util.TreeMap

/***
 *
 * @since 1.0.0
 * @author dark
 */
data class GrapheneMetric(
  val meta: MutableMap<String, String>,
  internal var tags: TreeMap<String, String>,
  internal var metrics: MutableMap<String, Double>,
  var timestampSeconds: Long
) : GraphiteAware {

  override fun getTags(): Map<String, String> {
    return this.tags
  }

  fun putTag(key: String, value: String) {
    tags[key] = value
  }

  fun putMetrics(key: String, value: Double) {
    metrics[key] = value
  }

  fun putMeta(key: String, value: String) {
    meta[key] = value
  }

  fun getId(): String {
    return "${meta.getOrDefault(TENANT, MetricRule.defaultTenant())}_${getGraphiteKey()}"
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
