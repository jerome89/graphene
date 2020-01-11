package com.graphene.reader.store.key.handler

import com.graphene.common.HierarchyMetricPaths
import com.graphene.common.beans.Path
import com.graphene.reader.service.index.KeySearchHandler
import com.graphene.reader.store.key.optimizer.ElasticsearchQueryOptimizer
import java.util.StringJoiner
import org.apache.logging.log4j.LogManager
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.search.SearchHit

/**
 *
 * @author dark
 * @author jerome89
 * @since 1.4.0
 */
class IndexBasedKeySearchHandler(
  private val elasticsearchClient: ElasticsearchClient,
  private val elasticsearchQueryOptimizer: ElasticsearchQueryOptimizer
) : KeySearchHandler {

  private val log = LogManager.getLogger(javaClass)

  override fun getPaths(tenant: String, pathExpressions: MutableList<String>, from: Long, to: Long): List<Path> {
    val result = mutableSetOf<Path>()
    for (pathExpression in pathExpressions) {
      val queryBuilder = elasticsearchQueryOptimizer.optimizeLeafQuery(pathExpression)
      queryThenAppend(result, queryBuilder, from, to)
    }
    return result.sortedWith(compareBy { it.path })
  }

  override fun getPathsByTags(tenant: String, tagExpressions: List<String>, from: Long, to: Long): List<Path> {
    log.info("Search paths by tags is not supported on SimpleKeySearchHandler.")
    return emptyList()
  }

  private fun queryThenAppend(result: MutableSet<Path>, queryBuilder: QueryBuilder, from: Long, to: Long) {
    val scrollIds = mutableListOf<String>()
    try {
      var response = elasticsearchClient.query(queryBuilder, from * 1000, to * 1000)

      while (response.hits.hits.isNotEmpty()) {
        for (hit in response.hits) {
          val depth = hit.sourceAsMap[DEPTH] as Int
          val stringJoiner = StringJoiner(DOT)
          for (i in 0 until depth) {
            stringJoiner.add(hit.sourceAsMap[index(i)] as String)
          }
          result.add(Path(stringJoiner.toString()))
        }

        response = elasticsearchClient.searchScroll(response)
        scrollIds.add(response.scrollId)
      }

      if (scrollIds.isNotEmpty()) {
        elasticsearchClient.clearScroll(scrollIds)
      }
    } catch (e: Exception) {
      log.warn("Search request is failed: " + e.message)
    }
  }

  override fun getHierarchyMetricPaths(tenant: String, pathExpression: String, from: Long, to: Long): MutableCollection<HierarchyMetricPaths.HierarchyMetricPath> {
    val result = mutableMapOf<String, HierarchyMetricPaths.HierarchyMetricPath>()

    try {
      var response = elasticsearchClient.query(elasticsearchQueryOptimizer.optimizeBranchQuery(pathExpression), from, to)
      val scrollIds = mutableListOf<String>()

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

          val hierarchyMetricPath = HierarchyMetricPaths.of(indexBasedKey.toString(), isLeaf(hit, maximumDepth))
          result[hierarchyMetricPath.text] = hierarchyMetricPath
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

    return result.values
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
