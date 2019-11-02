package com.graphene.reader.controller.graphite.request

data class MetricsFindRequest(
  val query: String,
  val from: Long?,
  val until: Long?
)
