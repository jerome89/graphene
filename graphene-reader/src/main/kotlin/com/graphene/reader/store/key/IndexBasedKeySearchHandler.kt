package com.graphene.reader.store.key

import com.graphene.common.HierarchyMetricPaths
import com.graphene.common.utils.PathExpressionUtils
import com.graphene.reader.service.index.KeySearchHandler
import com.graphene.reader.store.ElasticsearchClient
import com.graphene.reader.store.key.optimizer.ElasticsearchQueryOptimizer
import java.util.StringJoiner
import org.elasticsearch.search.SearchHit
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

/**
 *
 * @author dark
 */
@Component
@ConditionalOnProperty(prefix = "graphene.reader.store.key.handlers.index-based-key-search-handler", name = ["enabled"], havingValue = "true")
class IndexBasedKeySearchHandler(
  private val elasticsearchClient: ElasticsearchClient,
  private val elasticsearchQueryOptimizer: ElasticsearchQueryOptimizer
) : KeySearchHandler {

  // TODO Please fix me about time unit
  override fun getPaths(tenant: String, pathExpressions: MutableList<String>, from: Long, to: Long): MutableSet<String> {
    val result = mutableSetOf<String>()

    for (pathExpression in pathExpressions) {
      val scrollIds = mutableListOf<String>()

      var response = elasticsearchClient.query(elasticsearchQueryOptimizer.optimize(pathExpression), from * 1000, to * 1000)

      while (response.hits.hits.isNotEmpty()) {
        for (hit in response.hits) {
          val depth = hit.sourceAsMap[DEPTH] as Int
          val stringJoiner = StringJoiner(DOT)

          for (i in 0 until depth) {

            stringJoiner.add(hit.sourceAsMap[index(i)] as String)
          }

          result.add(stringJoiner.toString())
        }

        response = elasticsearchClient.searchScroll(response)
        scrollIds.add(response.scrollId)
      }

      if (scrollIds.isNotEmpty()) {
        elasticsearchClient.clearScroll(scrollIds)
      }
    }

    return result
  }

  private fun index(index: Int) = index.toString()

  override fun getHierarchyMetricPaths(tenant: String, pathExpression: String, from: Long, to: Long): MutableCollection<HierarchyMetricPaths.HierarchyMetricPath> {
    val escapedPathExpression = PathExpressionUtils.getEscapedPathExpression(pathExpression)

    var response = elasticsearchClient.query(elasticsearchQueryOptimizer.optimize(escapedPathExpression), from, to)

    var scrollIds = mutableListOf<String>()
    var result = mutableListOf<HierarchyMetricPaths.HierarchyMetricPath>()

    val maximumDepth = pathExpression.split(DOT).size

    while (response.hits.hits.isNotEmpty()) {
      for (hit in response.hits) {
        val stringJoiner = StringJoiner(DOT)

        for (i in 0 until maximumDepth) {
          stringJoiner.add(hit.sourceAsMap[index(i)] as String)
        }

        result.add(HierarchyMetricPaths.of(stringJoiner.toString(), isLeaf(hit, maximumDepth)))
      }

      response = elasticsearchClient.searchScroll(response)
      scrollIds.add(response.scrollId)
    }

    if (scrollIds.isNotEmpty()) {
      elasticsearchClient.clearScroll(scrollIds)
    }

    return result
  }

  private fun isLeaf(hit: SearchHit, maximumDepth: Int) =
    hit.sourceAsMap["depth"] as Int == maximumDepth

  companion object {
    const val DOT = "."
    const val DEPTH = "depth"
  }
}
