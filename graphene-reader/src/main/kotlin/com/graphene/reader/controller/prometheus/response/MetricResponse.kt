package com.graphene.reader.controller.prometheus.response

data class MetricResponse(
  var metric: MutableMap<String, String>,
  var values: MutableList<MutableList<Any>>
)
