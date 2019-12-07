package com.graphene.reader.store.key

import com.google.common.base.Joiner
import com.graphene.common.HierarchyMetricPaths
import com.graphene.common.utils.PathExpressionUtils
import com.graphene.reader.exceptions.TooMuchDataExpectedException
import com.graphene.reader.service.index.KeySearchHandler
import com.graphene.reader.store.ElasticsearchClient
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

/**
 *
 * @author Andrei Ivanov
 * @author dark
 */
@Component
@ConditionalOnProperty(prefix = "graphene.reader.store.key.handlers.simple-key-search-handler", name = ["enabled"], havingValue = "true")
class SimpleKeySearchHandler(
  private val elasticsearchClient: ElasticsearchClient
) : KeySearchHandler {

  @Throws(TooMuchDataExpectedException::class)
  override fun getPaths(tenant: String, pathExpressions: List<String>, from: Long, to: Long): Set<String> {
    val regExs = mutableListOf<String>()
    val result = mutableSetOf<String>()

    for (wildcard in pathExpressions) {
      if (PathExpressionUtils.isPlainPath(wildcard)) {
        result.add(wildcard)
      } else {
        regExs.add(PathExpressionUtils.getEscapedPathExpression(wildcard))
      }
    }

    logger.debug("getPaths plain paths: " + result.size + ", wildcard paths: " + regExs.size)

    if (0 < regExs.size) {
      val scrollIds = mutableListOf<String>()

      var response = elasticsearchClient.query(
        QueryBuilders.regexpQuery("path", Joiner.on("|").skipNulls().join(regExs)),
        from * 1000L,
        to * 1000L
      )

      while (response.hits.hits.isNotEmpty()) {
        for (hit in response.hits) {
          result.add(hit.sourceAsMap["path"] as String)
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
    internal val logger = LoggerFactory.getLogger(javaClass)
  }
}
