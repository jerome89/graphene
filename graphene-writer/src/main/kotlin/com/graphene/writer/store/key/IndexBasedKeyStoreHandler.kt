package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.key.model.IndexBasedKeyStoreHandlerProperty
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest
import org.elasticsearch.action.index.IndexRequest
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
  val property: IndexBasedKeyStoreHandlerProperty
) : AbstractElasticsearchKeyStoreHandler(property) {

  override fun createTemplateIfNotExists() {
    val putIndexTemplateRequest = PutIndexTemplateRequest(SimpleKeyStoreHandler.TEMPLATE_NAME)
    putIndexTemplateRequest.patterns(listOf(property.templateIndexPattern))
    putIndexTemplateRequest.source(SimpleKeyStoreHandler.SOURCE, XContentType.JSON)

    getElasticsearchClient().putTemplate(putIndexTemplateRequest)
  }

  override fun mapToIndexRequests(metric: GrapheneMetric?): List<IndexRequest> {
    val indexRequests = mutableListOf<IndexRequest>()
    indexRequests.add(IndexRequest(currentIndexPointer(), property.type, metric!!.getId()).source(source(metric)))
    return indexRequests
  }

  override fun createIndexIfNotExists(index: String) {
    getElasticsearchClient().createIndexIfNotExists("$index.0")
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
