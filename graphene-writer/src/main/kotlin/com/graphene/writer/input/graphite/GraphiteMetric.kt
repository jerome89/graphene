package com.graphene.writer.input.graphite

data class GraphiteMetric(
  val key: String,
  val value: Double,
  val timestamp: Long
)
