package com.graphene.reader.store.key

import com.graphene.common.HierarchyMetricPaths
import com.graphene.reader.service.index.KeySearchHandler
import com.graphene.reader.store.ElasticsearchClient
import com.graphene.reader.store.key.optimizer.ElasticsearchQueryOptimizer
import java.util.StringJoiner
import org.apache.logging.log4j.LogManager
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

  internal val log = LogManager.getLogger(javaClass)

  override fun getPaths(tenant: String, pathExpressions: MutableList<String>, from: Long, to: Long): MutableSet<String> {
    val result = mutableSetOf<String>()

    for (pathExpression in pathExpressions) {
      val scrollIds = mutableListOf<String>()

      var response = elasticsearchClient.query(elasticsearchQueryOptimizer.optimizeLeafQuery(pathExpression), from * 1000, to * 1000)

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

  override fun getHierarchyMetricPaths(tenant: String, pathExpression: String, from: Long, to: Long): MutableCollection<HierarchyMetricPaths.HierarchyMetricPath> {
    var result = mutableSetOf<HierarchyMetricPaths.HierarchyMetricPath>()

    try {
      var response = elasticsearchClient.query(elasticsearchQueryOptimizer.optimizeBranchQuery(pathExpression), from, to)
      var scrollIds = mutableListOf<String>()

      val maximumDepth = pathExpression.split(DOT).size

      while (response.hits.hits.isNotEmpty()) {
        for (hit in response.hits) {
          if (isLowDepth(hit, maximumDepth)) {
            continue
          }

          val indexBasedKey = StringJoiner(DOT)
          for (i in 0 until maximumDepth) {
            indexBasedKey.add(hit.sourceAsMap[index(i)] as String)
          }

          result.add(HierarchyMetricPaths.of(indexBasedKey.toString(), isLeaf(hit, maximumDepth)))
        }

        response = elasticsearchClient.searchScroll(response)
        scrollIds.add(response.scrollId)
      }

      if (scrollIds.isNotEmpty()) {
        elasticsearchClient.clearScroll(scrollIds)
      }
    } catch (e: Throwable) {
      log.error("Fail to get hierarchy metric paths : $pathExpression")
      throw e
    }

    return result
  }

  private fun isLowDepth(hit: SearchHit, maximumDepth: Int) =
    (hit.sourceAsMap["depth"] as Int) < maximumDepth

  private fun index(index: Int) = index.toString()

  private fun isLeaf(hit: SearchHit, maximumDepth: Int) =
    hit.sourceAsMap["depth"] as Int == maximumDepth

  companion object {
    const val DOT = "."
    const val DEPTH = "depth"
  }
}
