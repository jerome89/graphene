package com.graphene.reader.controller.prometheus.response

data class QueryRangeResponseData(
  var resultType: String,
  var result: MutableList<MetricResponse> = mutableListOf()
)
