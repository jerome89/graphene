package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.key.model.ElasticsearchFactory
import com.graphene.writer.store.key.model.SimpleKeyStoreHandlerProperty
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.get.GetIndexRequest
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.common.xcontent.XContentType
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
  val elasticsearchFactory: ElasticsearchFactory,
  val property: SimpleKeyStoreHandlerProperty
) : AbstractElasticsearchKeyStoreHandler(elasticsearchFactory, property) {

  override fun mapToIndexRequests(metric: GrapheneMetric?): List<IndexRequest> {
    if (Objects.isNull(metric)) {
      return Collections.emptyList<IndexRequest>()
    }

    val indexRequests = mutableListOf<IndexRequest>()
    val parts = metric!!.getGraphiteKeyParts()
    val graphiteKeySb = StringBuilder()

    for (depth in parts.indices) {
      if (graphiteKeySb.toString().isNotEmpty()) {
        graphiteKeySb.append(".")
      }
      graphiteKeySb.append(parts[depth])
      try {
        val graphiteKeyPart = graphiteKeySb.toString()
        indexRequests.add(IndexRequest(property.index, property.type, metric.getTenant() + "_" + graphiteKeyPart)
          .source(source(metric.getTenant(), graphiteKeyPart, depth, isLeaf(depth, parts))))
      } catch (e: Exception) {
        throw IllegalStateException("Invokes illegal state in map to index requests", e)
      }
    }

    return indexRequests
  }

  override fun createIndexIfNotExists(index: String) {
    val restHighLevelClient = elasticsearchFactory.restHighLevelClient()

    if (restHighLevelClient.indices().exists(GetIndexRequest().indices(index), RequestOptions.DEFAULT)) {
      return
    }

    val createIndexRequest = CreateIndexRequest(index)
    restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT)
  }

  override fun createTemplateIfNotExists(index: String) {
    val restHighLevelClient = elasticsearchFactory.restHighLevelClient()
    val putIndexTemplateRequest = PutIndexTemplateRequest("simple-key-path-template")
    putIndexTemplateRequest.patterns(listOf("simple-key-path*"))
    putIndexTemplateRequest.source(SOURCE, XContentType.JSON)

    restHighLevelClient.indices().putTemplate(putIndexTemplateRequest, RequestOptions.DEFAULT)
  }

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

    const val SOURCE = """
      {
        "index_patterns": ["simple-key-path*"],
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
