package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.key.model.ElasticsearchFactory
import com.graphene.writer.store.key.model.IndexBasedKeyStoreHandlerProperty
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

/**
 *
 * @author dark
 * @since 1.0.0
 */
@Component
@ConditionalOnProperty(prefix = "graphene.writer.store.key.handlers.index-based-key-store-handler", name = ["enabled"], havingValue = "true")
class IndexBasedKeyStoreHandler(
  val elasticsearchFactory: ElasticsearchFactory,
  val property: IndexBasedKeyStoreHandlerProperty
) : AbstractElasticsearchKeyStoreHandler(elasticsearchFactory, property) {

  override fun createTemplateIfNotExists(): PutIndexTemplateRequest {
    val putIndexTemplateRequest = PutIndexTemplateRequest(TEMPLATE_NAME)
    putIndexTemplateRequest.patterns(listOf(property.templateIndexPattern))
    putIndexTemplateRequest.source(SOURCE, XContentType.JSON)
    return putIndexTemplateRequest
  }

  override fun mapToIndexRequests(metric: GrapheneMetric?): List<IndexRequest> {
    val indexRequests = mutableListOf<IndexRequest>()
    indexRequests.add(IndexRequest(property.index, property.type, metric!!.getId()).source(source(metric)))
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

  private fun source(metric: GrapheneMetric): XContentBuilder {
    val graphiteKeyParts = metric.getGraphiteKeyParts()

    val source = XContentFactory.jsonBuilder()
      .startObject()
      .field(TENANT, metric.getTenant())
      .field(DEPTH, graphiteKeyParts.size)
      .field(LEAF, true)

    for (index in graphiteKeyParts.indices) {
      source.field(index.toString(), graphiteKeyParts[index])
    }

    return source.endObject()
  }

  companion object {
    const val TENANT = "tenant"
    const val DEPTH = "depth"
    const val LEAF = "leaf"

    const val TEMPLATE_NAME = "index-based-key-path-template"
    const val SOURCE = """
      {
        "settings": {
          "number_of_replicas": 0,
          "number_of_shards": 5
        }
      }
    """
  }

}
