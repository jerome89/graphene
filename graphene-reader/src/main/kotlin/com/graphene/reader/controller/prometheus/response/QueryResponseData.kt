package com.graphene.reader.controller.prometheus.response

data class QueryResponseData(
  var resultType: String,
  var result: MutableList<Any> = mutableListOf()
)
