package com.graphene.writer.input

import com.graphene.writer.input.graphite.GraphiteAware
import net.iponweb.disthene.reader.utils.MetricRule

/***
 *
 * @since 1.0.0
 * @author dark
 */
data class GrapheneMetric(
  val meta: Map<String, String>,
  internal val tags: Map<String, String>,
  val value: Double,
  val timestampSeconds: Long
) : GraphiteAware {

  override fun getTags(): Map<String, String> {
    return this.tags
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
