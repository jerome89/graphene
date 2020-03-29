package com.graphene.reader.store.tag.elasticsearch.optimizer

data class TagSearchTarget(
  var tagKey: String? = null,
  var tagValue: String? = null,
  var tagExpressions: List<String> = emptyList()
)
