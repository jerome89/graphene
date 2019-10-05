package com.graphene.reader.service.index

import com.google.common.base.Joiner
import com.graphene.common.HierarchyMetricPaths
import net.iponweb.disthene.reader.exceptions.TooMuchDataExpectedException
import net.iponweb.disthene.reader.service.index.IndexService
import net.iponweb.disthene.reader.utils.WildcardUtil
import org.elasticsearch.index.query.FilterBuilders
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

/**
 *
 * @author Andrei Ivanov
 * @author dark
 */
@Component
class ElasticsearchIndexService(
  private val elasticsearchClient: ElasticsearchClient
) : IndexService {

  private val joiner = Joiner.on(",").skipNulls()

  @Throws(TooMuchDataExpectedException::class)
  override fun getPaths(tenant: String, wildcards: List<String>): Set<String> {
    val regExs = ArrayList<String>()
    val result = HashSet<String>()

    for (wildcard in wildcards) {
      if (WildcardUtil.isPlainPath(wildcard)) {
        result.add(wildcard)
      } else {
        regExs.add(WildcardUtil.getPathsRegExFromWildcard(wildcard))
      }
    }

    logger.debug("getPaths plain paths: " + result.size + ", wildcard paths: " + regExs.size)

    if (regExs.size > 0) {
      val regEx = Joiner.on("|").skipNulls().join(regExs)

      var response = elasticsearchClient.query(
        QueryBuilders.regexpQuery("path", regEx)
      )

      while (response.hits.hits.isNotEmpty()) {
        for (hit in response.hits) {
          result.add(hit.sourceAsMap()["path"] as String)
        }

        response = elasticsearchClient.searchScroll(response)
      }
    }

    return result
  }

  @Throws(TooMuchDataExpectedException::class)
  override fun getHierarchyMetricPaths(tenant: String, query: String): Collection<HierarchyMetricPaths.HierarchyMetricPath> {
    val hierarchyMetricPaths = mutableMapOf<String, HierarchyMetricPaths.HierarchyMetricPath>()
    try {
      val regEx = WildcardUtil.getPathsRegExFromWildcard(query)

      var response = elasticsearchClient.query(
        QueryBuilders.regexpQuery("path", regEx)
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
    val source = hit.sourceAsMap()

    val path = source["path"] as String
    val leaf = source["leaf"] as Boolean

    return HierarchyMetricPaths.of(path, leaf)
  }

  @Throws(TooMuchDataExpectedException::class)
  fun getPathsAsJsonArray(tenant: String, wildcard: String): String {
    val regEx = WildcardUtil.getPathsRegExFromWildcard(wildcard)

    var response = elasticsearchClient.query(
      QueryBuilders.filteredQuery(
        QueryBuilders.regexpQuery("path", regEx),
        FilterBuilders.termFilter("tenant", tenant)
      )
    )

    val paths = ArrayList<String>()
    while (response.hits.hits.isNotEmpty()) {
      for (hit in response.hits) {
        paths.add(hit.sourceAsString)
      }
      response = elasticsearchClient.searchScroll(response)
    }

    return "[" + joiner.join(paths) + "]"
  }

  companion object {
    internal val logger = LoggerFactory.getLogger(ElasticsearchIndexService::class.java)
  }
}
