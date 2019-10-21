package com.graphene.reader.store.key

import com.google.common.base.Joiner
import com.graphene.common.HierarchyMetricPaths
import com.graphene.reader.service.index.ElasticsearchClient
import net.iponweb.disthene.reader.exceptions.TooMuchDataExpectedException
import net.iponweb.disthene.reader.service.index.IndexService
import net.iponweb.disthene.reader.utils.WildcardUtil
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
@ConditionalOnProperty(prefix = "graphene.reader.store.key.handlers.elasticsearch-key-search-handler", name = ["enabled"], havingValue = "true")
class ElasticsearchKeySearchHandler(
  private val elasticsearchClient: ElasticsearchClient
) : IndexService {

  @Throws(TooMuchDataExpectedException::class)
  override fun getPaths(tenant: String, wildcards: List<String>): Set<String> {
    val regExs = mutableListOf<String>()
    val result = mutableSetOf<String>()

    for (wildcard in wildcards) {
      if (WildcardUtil.isPlainPath(wildcard)) {
        result.add(wildcard)
      } else {
        regExs.add(WildcardUtil.getPathsRegExFromWildcard(wildcard))
      }
    }

    logger.debug("getPaths plain paths: " + result.size + ", wildcard paths: " + regExs.size)

    if (0 < regExs.size) {
      val scrollIds = mutableListOf<String>()

      var response = elasticsearchClient.query(
        QueryBuilders.regexpQuery("path", Joiner.on("|").skipNulls().join(regExs))
      )

      while (response.hits.hits.isNotEmpty()) {
        for (hit in response.hits) {
          result.add(hit.sourceAsMap["path"] as String)
        }

        response = elasticsearchClient.searchScroll(response)
        scrollIds.add(response.scrollId)
      }

      elasticsearchClient.clearScroll(scrollIds)
    }

    return result
  }

  @Throws(TooMuchDataExpectedException::class)
  override fun getHierarchyMetricPaths(tenant: String, query: String): Collection<HierarchyMetricPaths.HierarchyMetricPath> {
    val hierarchyMetricPaths = mutableMapOf<String, HierarchyMetricPaths.HierarchyMetricPath>()
    try {
      var response = elasticsearchClient.query(
        QueryBuilders.regexpQuery("path", WildcardUtil.getPathsRegExFromWildcard(query))
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
    internal val logger = LoggerFactory.getLogger(ElasticsearchKeySearchHandler::class.java)
  }
}
