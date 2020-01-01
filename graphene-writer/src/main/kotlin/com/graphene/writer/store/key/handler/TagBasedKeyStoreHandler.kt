package com.graphene.writer.store.key.handler

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.key.ElasticsearchClientFactory
import com.graphene.writer.store.key.GrapheneIndexRequest
import com.graphene.writer.store.key.KeyStoreHandlerProperty
import java.util.TreeMap
import org.apache.logging.log4j.LogManager
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory

class TagBasedKeyStoreHandler(
  private val elasticsearchClientFactory: ElasticsearchClientFactory,
  val property: KeyStoreHandlerProperty
) : AbstractElasticsearchKeyStoreHandler(elasticsearchClientFactory, property) {

  private val log = LogManager.getLogger(javaClass)

  override fun mapToGrapheneIndexRequests(metric: GrapheneMetric?): List<GrapheneIndexRequest> {
    val grapheneIndexRequests = mutableListOf<GrapheneIndexRequest>()
    grapheneIndexRequests.add(GrapheneIndexRequest("${metric!!.id}", source(metric.tags, metric), metric.timestampMillis()))
    return grapheneIndexRequests
  }

  override fun templateSource(): String = SOURCE

  override fun templateName(): String = TEMPLATE_NAME

  private fun source(tags: TreeMap<String, String>, grapheneMetric: GrapheneMetric): XContentBuilder {
    val source = XContentFactory.jsonBuilder()
      .startObject()

    for (tag in tags) {
      source.field(tag.key, tag.value)
    }

    source.field("@name", grapheneMetric.metricKey())

    return source.endObject()
  }

  companion object {
    const val TEMPLATE_NAME = "tag-based-key-path-template"
    const val SOURCE = """
      {
        "settings": {
          "number_of_replicas": 0,
          "number_of_shards": 5,
          "auto_expand_replicas": "0-1"
        },
        "mappings": {
          "path": {
            "dynamic_templates": [
              {
                "else": {
                  "match": "*",
                  "unmatch": "depth",
                  "match_mapping_type": "string",
                  "mapping": {
                    "type": "keyword"
                  }
                }
              }
            ]
          }
        }
      }
    """
  }
}
