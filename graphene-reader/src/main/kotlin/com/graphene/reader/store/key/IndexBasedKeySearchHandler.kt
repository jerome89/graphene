package com.graphene.reader.store.key

import com.graphene.common.HierarchyMetricPaths
import com.graphene.reader.service.index.KeySearchHandler
import com.graphene.reader.store.ElasticsearchClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

/**
 *
 * @author dark
 */
@Component
@ConditionalOnProperty(prefix = "graphene.reader.store.key.handlers.index-based-key-search-handler", name = ["enabled"], havingValue = "true")
class IndexBasedKeySearchHandler(
  private val elasticsearchClient: ElasticsearchClient
): KeySearchHandler {

  override fun getPaths(tenant: String, wildcards: MutableList<String>, from: Long, to: Long): MutableSet<String> {
    return mutableSetOf()
  }

  override fun getHierarchyMetricPaths(tenant: String, query: String, from: Long, to: Long): MutableCollection<HierarchyMetricPaths.HierarchyMetricPath> {
    return mutableListOf()
  }

}
