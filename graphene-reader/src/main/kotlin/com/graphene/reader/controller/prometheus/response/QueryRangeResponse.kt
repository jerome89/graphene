package com.graphene.reader.controller.prometheus.response

data class QueryRangeResponse(
  var status: String,
  var data: QueryRangeResponseData
)
