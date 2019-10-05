package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory

class GrapheneKeyMapper {

  fun mapGrapheneMetricKey(metric: GrapheneMetric, sb: StringBuilder, i: Int, parts: List<String>): XContentBuilder {
    return XContentFactory.jsonBuilder()
      .startObject()
      .field("@tenant", metric.getTenant())
      .field("path", sb.toString())
      .field("depth", i + 1)
      .field("leaf", i == parts.size - 1)
      .endObject()
  }
}
