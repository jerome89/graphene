package com.graphene.reader.controller.prometheus.response

data class QueryResponse(
  var status: String,
  var data: QueryResponseData
)
