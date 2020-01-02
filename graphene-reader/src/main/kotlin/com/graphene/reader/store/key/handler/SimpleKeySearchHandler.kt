package com.graphene.reader.store.key.handler

import com.google.common.base.Joiner
import com.graphene.common.HierarchyMetricPaths
import com.graphene.common.beans.Path
import com.graphene.common.utils.PathExpressionUtils
import com.graphene.reader.exceptions.TooMuchDataExpectedException
import com.graphene.reader.service.index.KeySearchHandler
import com.graphene.reader.store.tag.optimizer.ElasticsearchTagSearchQueryOptimizer
import com.graphene.reader.store.tag.optimizer.TagSearchTarget
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit
import org.slf4j.LoggerFactory

/**
 *
 * @author Andrei Ivanov
 * @author dark
 */
class SimpleKeySearchHandler(
  private val elasticsearchClient: ElasticsearchClient,
  private val elasticsearchTagSearchQueryOptimizer: ElasticsearchTagSearchQueryOptimizer
) : KeySearchHandler {

  @Throws(TooMuchDataExpectedException::class)
  override fun getPaths(tenant: String, pathExpressions: List<String>, from: Long, to: Long): List<Path> {
    val regExs = mutableListOf<String>()
    val plains = mutableListOf<String>()
    val result = mutableSetOf<Path>()

    for (pathExpression in pathExpressions) {
      if (PathExpressionUtils.isPlainPath(pathExpression)) {
        plains.add(pathExpression)
      } else {
        regExs.add(PathExpressionUtils.getEscapedPathExpression(pathExpression))
      }
    }

    logger.debug("getPaths plain paths: " + result.size + ", wildcard paths: " + regExs.size)

    if (0 < regExs.size) {
      val queryBuilder = QueryBuilders.regexpQuery("path", Joiner.on("|").skipNulls().join(regExs))
      queryAndAppendToResult(result, queryBuilder, from, to)
    }

    if (0 < plains.size) {
      val queryBuilder = QueryBuilders.termsQuery("path", plains)
      queryAndAppendToResult(result, queryBuilder, from, to)
    }

    return result.sortedWith(compareBy { it.path })
  }

  override fun getPathsByTags(tenant: String, tagExpressions: List<String>, from: Long, to: Long): List<Path> {
    val result = mutableSetOf<Path>()
    val queryBuilder = elasticsearchTagSearchQueryOptimizer.optimize(TagSearchTarget(tagExpressions = tagExpressions))
    queryAndAppendToResult(result, queryBuilder, from, to)
    return result.sortedWith(compareBy { it.path })
  }

  private fun queryAndAppendToResult(result: MutableSet<Path>, queryBuilder: QueryBuilder, from: Long, to: Long) {
    val scrollIds = mutableListOf<String>()
    try {
      var response = elasticsearchClient.query(
        queryBuilder,
        from * 1000L,
        to * 1000L
      )

      while (response.hits.hits.isNotEmpty()) {
        for (hit in response.hits) {
          val path = Path(hit.sourceAsMap["path"] as String)
          if (hit.sourceAsMap[TAGS_BUCKET_NAME] != null) {
            val tags = (hit.sourceAsMap[TAGS_BUCKET_NAME] as Map<String, *>)
            for (tag in tags) {
              path.addTag(tag.key, tag.value.toString())
            }
          }
          result.add(path)
        }

        if (response.scrollId.isNotBlank()) {
          response = elasticsearchClient.searchScroll(response)
          scrollIds.add(response.scrollId)
        }
      }

      if (scrollIds.isNotEmpty()) {
        elasticsearchClient.clearScroll(scrollIds)
      }
    } catch (e: Exception) {
      logger.warn("Search request failed: " + e.message)
    }
  }

  @Throws(TooMuchDataExpectedException::class)
  override fun getHierarchyMetricPaths(tenant: String, pathExpression: String, from: Long, to: Long): Collection<HierarchyMetricPaths.HierarchyMetricPath> {
    val hierarchyMetricPaths = mutableMapOf<String, HierarchyMetricPaths.HierarchyMetricPath>()
    try {
      var response = elasticsearchClient.query(
        QueryBuilders.regexpQuery("path", PathExpressionUtils.getEscapedPathExpression(pathExpression)),
        from,
        to
      )

      while (response.hits.hits.isNotEmpty()) {
        for (hit in response.hits) {
          val hierarchyMetricPath = mapToHierarchyMetricPath(hit)
          hierarchyMetricPaths.putIfAbsent(hierarchyMetricPath.text, hierarchyMetricPath)
        }
        response = elasticsearchClient.searchScroll(response)
      }
    } catch (e: Exception) {
      logger.error("Fail to get paths : " + e.message, e)
    }

    return hierarchyMetricPaths.values
  }

  private fun mapToHierarchyMetricPath(hit: SearchHit): HierarchyMetricPaths.HierarchyMetricPath {
    val source = hit.sourceAsMap

    val path = source["path"] as String
    val leaf = source["leaf"] as Boolean

    return HierarchyMetricPaths.of(path, leaf)
  }

  companion object {
    const val TAGS_BUCKET_NAME = "tags"
    internal val logger = LoggerFactory.getLogger(javaClass)
  }
}
