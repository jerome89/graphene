package com.graphene.writer.store.key

import org.elasticsearch.common.xcontent.XContentBuilder

data class GrapheneIndexRequest(
  val id: String,
  val source: XContentBuilder
)
