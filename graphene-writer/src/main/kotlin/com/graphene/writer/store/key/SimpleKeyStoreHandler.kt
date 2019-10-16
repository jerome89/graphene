package com.graphene.writer.store.key

import com.graphene.writer.store.key.model.ElasticsearchFactory
import com.graphene.writer.store.key.model.SimpleKeyStoreHandlerProperty
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

/**
 * @author Andrei Ivanov
 * @author dark
 */
@Component
@ConditionalOnProperty(prefix = "graphene.writer.store.key.handlers.simple-key-store-handler", name = ["enabled"], havingValue = "true")
class SimpleKeyStoreHandler(
  elasticsearchFactory: ElasticsearchFactory,
  property: SimpleKeyStoreHandlerProperty
) : AbstractElasticsearchKeyStoreHandler(elasticsearchFactory, property) {

  override fun source(tenant: String, graphiteKeyPart: String, depth: Int, leaf: Boolean): XContentBuilder {
    return XContentFactory.jsonBuilder()
      .startObject()
      .field(TENANT, tenant)
      .field(PATH, graphiteKeyPart)
      .field(DEPTH, depth + 1)
      .field(LEAF, leaf)
      .endObject()
  }

  companion object {
    const val TENANT = "tenant"
    const val DEPTH = "tenant"
    const val LEAF = "leaf"
    const val PATH = "path"
  }

}
