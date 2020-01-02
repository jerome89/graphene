package com.graphene.reader.store.key.handler

import com.graphene.common.HierarchyMetricPaths
import com.graphene.common.beans.Path
import com.graphene.reader.service.index.KeySearchHandler
import com.graphene.reader.store.key.optimizer.ElasticsearchQueryOptimizer
import com.graphene.reader.store.tag.optimizer.ElasticsearchTagSearchQueryOptimizer
import com.graphene.reader.store.tag.optimizer.TagSearchTarget
import java.util.Objects
import java.util.StringJoiner
import org.apache.logging.log4j.LogManager
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.search.SearchHit

/**
 *
 * @author dark
 * @since 1.4.0
 */
class IndexBasedKeySearchHandler(
  private val elasticsearchClient: ElasticsearchClient,
  private val elasticsearchQueryOptimizer: ElasticsearchQueryOptimizer,
  private val elasticsearchTagSearchQueryOptimizer: ElasticsearchTagSearchQueryOptimizer
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
    val result = mutableSetOf<Path>()
    val queryBuilder = elasticsearchTagSearchQueryOptimizer.optimize(TagSearchTarget(tagExpressions = tagExpressions))
    queryThenAppend(result, queryBuilder, from, to)
    return result.sortedWith(compareBy { it.path })
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

          val path = Path(stringJoiner.toString())
          if (Objects.nonNull(hit.sourceAsMap[TAGS_BUCKET_NAME])) {
            val tags = (hit.sourceAsMap[TAGS_BUCKET_NAME] as Map<String, *>)
            for (tag in tags) {
              path.addTag(tag.key, tag.value.toString())
            }
          }
          result.add(path)
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
      var response = elasticsearchClient.query(elasticsearchQueryOptimizer.optimizeBranchQuery(pathExpression), from, to, "")
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
    const val TAGS_BUCKET_NAME = "tags"
    const val DOT = "."
    const val DEPTH = "depth"
  }
}
