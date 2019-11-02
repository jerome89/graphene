package com.graphene.writer.store.key.handler

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.key.ElasticsearchClientFactory
import com.graphene.writer.store.key.GrapheneIndexRequest
import com.graphene.writer.store.key.property.IndexBasedKeyStoreHandlerProperty
import com.graphene.writer.store.key.property.RotationProperty
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory
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
  val elasticsearchClientFactory: ElasticsearchClientFactory,
  val rotationProperty: RotationProperty,
  val property: IndexBasedKeyStoreHandlerProperty
) : AbstractElasticsearchKeyStoreHandler(elasticsearchClientFactory, rotationProperty, property) {

  override fun mapToGrapheneIndexRequests(metric: GrapheneMetric?): List<GrapheneIndexRequest> {
    return mutableListOf(GrapheneIndexRequest(metric!!.getId(), source(metric)))
  }

  override fun templateSource(): String = SOURCE

  override fun templateName(): String = TEMPLATE_NAME

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
