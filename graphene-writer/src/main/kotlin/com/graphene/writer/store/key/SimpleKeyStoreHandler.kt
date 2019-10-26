package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.key.model.ElasticsearchClientFactory
import com.graphene.writer.store.key.model.SimpleKeyStoreHandlerProperty
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author Andrei Ivanov
 * @author dark
 */
@Component
@ConditionalOnProperty(prefix = "graphene.writer.store.key.handlers.simple-key-store-handler", name = ["enabled"], havingValue = "true")
class SimpleKeyStoreHandler(
  val elasticsearchClientFactory: ElasticsearchClientFactory,
  val property: SimpleKeyStoreHandlerProperty
) : AbstractElasticsearchKeyStoreHandler(elasticsearchClientFactory, property) {

  override fun mapToGrapheneIndexRequests(metric: GrapheneMetric?): List<GrapheneIndexRequest> {
    if (Objects.isNull(metric)) {
      return Collections.emptyList<GrapheneIndexRequest>()
    }

    val grapheneIndexRequests = mutableListOf<GrapheneIndexRequest>()
    val parts = metric!!.getGraphiteKeyParts()
    val graphiteKeySb = StringBuilder()

    for (depth in parts.indices) {
      if (graphiteKeySb.toString().isNotEmpty()) {
        graphiteKeySb.append(".")
      }
      graphiteKeySb.append(parts[depth])
      try {
        val graphiteKeyPart = graphiteKeySb.toString()
        val id = "${metric.getTenant()}_$graphiteKeyPart"
        grapheneIndexRequests.add(GrapheneIndexRequest(id, source(metric.getTenant(), graphiteKeyPart, depth, isLeaf(depth, parts))))
      } catch (e: Exception) {
        throw IllegalStateException("Invokes illegal state in map to index requests", e)
      }
    }

    return grapheneIndexRequests
  }

  override fun templateName(): String = TEMPLATE_NAME

  override fun templateSource(): String = SOURCE

  private fun source(tenant: String, graphiteKeyPart: String, depth: Int, leaf: Boolean): XContentBuilder {
    return XContentFactory.jsonBuilder()
      .startObject()
      .field(TENANT, tenant)
      .field(PATH, graphiteKeyPart)
      .field(DEPTH, depth + 1)
      .field(LEAF, leaf)
      .endObject()
  }

  private fun isLeaf(depth: Int, parts: List<String>) = depth == parts.size - 1

  companion object {
    const val TENANT = "tenant"
    const val DEPTH = "depth"
    const val LEAF = "leaf"
    const val PATH = "path"

    const val TEMPLATE_NAME = "simple-key-path-template"
    const val SOURCE = """
      {
        "settings": {
          "number_of_replicas": 0,
          "number_of_shards": 5
        },
        "mappings": {
          "path": {
            "properties": {
              "path": {
                "type": "keyword"
              },
              "depth": {
                "type": "long"
              },
              "leaf": {
                "type": "boolean"
              },
              "tenant": {
                "type": "keyword"
              }
            }
          }
        }
      }
    """
  }

}
