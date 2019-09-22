package com.graphene.writer.input

/***
 *
 * @since 1.0.0
 * @author dark
 */
data class GrapheneMetric(
  val meta: Map<String, String>,
  val tags: Map<String, String>,
  val value: Double,
  val timestamp: Long
)
