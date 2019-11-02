package com.graphene.reader.controller.graphite.request

data class MetricsFindRequest(
  val query: String,
  val from: String?,
  val to: String?
)
