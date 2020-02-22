package com.graphene.reader.controller.prometheus.request

data class QueryRangeRequest(
  var query: String,
  var start: Long,
  var end: Long,
  var step: Int
)
