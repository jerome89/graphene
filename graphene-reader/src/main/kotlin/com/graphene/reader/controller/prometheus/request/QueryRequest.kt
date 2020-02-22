package com.graphene.reader.controller.prometheus.request

data class QueryRequest(
  var query: String,
  var time: Double
)
