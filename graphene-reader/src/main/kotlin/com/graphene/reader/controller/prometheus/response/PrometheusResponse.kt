package com.graphene.reader.controller.prometheus.response

data class PrometheusResponse(
  var status: String,
  var data: List<String>
)
