package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.key.model.ElasticsearchFactory
import com.graphene.writer.store.key.model.IndexBasedKeyStoreHandlerProperty
import org.elasticsearch.action.index.IndexRequest
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
  elasticsearchFactory: ElasticsearchFactory,
  property: IndexBasedKeyStoreHandlerProperty
) : AbstractElasticsearchKeyStoreHandler(elasticsearchFactory, property) {

  override fun mapToIndexRequests(metric: GrapheneMetric?): List<IndexRequest> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createIndexIfNotExists(index: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createTemplateIfNotExists(index: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun source(tenant: String, graphiteKeyPart: String, depth: Int, leaf: Boolean): XContentBuilder {
    val source = XContentFactory.jsonBuilder()
      .startObject()
      .field(TENANT, tenant)
      .field(DEPTH, depth + 1)
      .field(LEAF, leaf)

    val graphiteKeyParts = graphiteKeyPart.split(DOT)
    for (index in graphiteKeyParts.indices) {
      source.field(index.toString(), graphiteKeyParts[index])
    }

    return source.endObject()
  }

  companion object {
    const val TENANT = "tenant"
    const val DEPTH = "depth"
    const val LEAF = "leaf"
    const val DOT = "."
  }

}
