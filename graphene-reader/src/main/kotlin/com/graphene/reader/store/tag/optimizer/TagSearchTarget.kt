package com.graphene.reader.store.tag.optimizer

data class TagSearchTarget(
  var tagKey: String? = null,
  var tagValue: String? = null,
  var tagExpressions: List<String> = emptyList()
)
