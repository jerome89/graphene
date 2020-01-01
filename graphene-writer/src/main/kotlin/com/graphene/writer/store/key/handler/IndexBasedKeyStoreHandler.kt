package com.graphene.writer.store.key.handler

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.key.ElasticsearchClientFactory
import com.graphene.writer.store.key.GrapheneIndexRequest
import com.graphene.writer.store.key.KeyStoreHandlerProperty
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory

/**
 *
 * @author dark
 * @since 1.0.0
 */
class IndexBasedKeyStoreHandler(
  private val elasticsearchClientFactory: ElasticsearchClientFactory,
  val property: KeyStoreHandlerProperty
) : AbstractElasticsearchKeyStoreHandler(elasticsearchClientFactory, property) {

  override fun mapToGrapheneIndexRequests(metric: GrapheneMetric?): List<GrapheneIndexRequest> {
    return mutableListOf(GrapheneIndexRequest(metric!!.id!!, source(metric), metric.timestampMillis()))
  }

  override fun templateSource(): String = SOURCE

  override fun templateName(): String = TEMPLATE_NAME

  private fun source(metric: GrapheneMetric): XContentBuilder {
    val tags = metric.tags

    val source = XContentFactory.jsonBuilder()
      .startObject()
      .field(DEPTH, tags.size)
      .field(LEAF, true)

    for (tag in tags) {
      source.field(tag.key, tag.value)
    }

    return source.endObject()
  }

  companion object {
    const val DEPTH = "depth"
    const val LEAF = "leaf"

    const val TEMPLATE_NAME = "index-based-key-path-template"
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
                "leaf": {
                  "match": "leaf",
                  "match_mapping_type": "boolean",
                  "mapping": {
                    "type": "boolean"
                  }
                }
              },
              {
                "depth": {
                  "match": "depth",
                  "match_mapping_type": "long",
                  "mapping": {
                    "type": "integer"
                  }
                }
              },
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
