package com.graphene.reader.service.index

import com.google.common.base.Joiner
import net.iponweb.disthene.reader.config.IndexConfiguration
import net.iponweb.disthene.reader.exceptions.TooMuchDataExpectedException
import com.graphene.common.HierarchyMetricPaths
import net.iponweb.disthene.reader.service.index.IndexService
import net.iponweb.disthene.reader.utils.WildcardUtil
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.FilterBuilders
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import javax.annotation.PreDestroy
import java.util.*

/**
 *
 * @author Andrei Ivanov
 * @author dark
 */
@Component
class ElasticsearchIndexService(
  private val client: TransportClient,
  private val indexConfiguration: IndexConfiguration,
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

      // Why secondary searchPaths to elasticsearch?
      // Reason : https://stackoverflow.com/questions/18239537/scroll-searchresponse-not-iterable-when-there-are-less-results-than-the-scrollsi
      var response = elasticsearchClient.actionGet(regEx)

      while (response.hits.hits.size > 0) {
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

      // Why secondary searchPaths to elasticsearch?
      // Reason : https://stackoverflow.com/questions/18239537/scroll-searchresponse-not-iterable-when-there-are-less-results-than-the-scrollsi
      var response = elasticsearchClient.actionGet(regEx)

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

    var response = client
      .prepareSearch(indexConfiguration.index)
      .setScroll(TimeValue(indexConfiguration.timeout.toLong()))
      .setSize(indexConfiguration.scroll)
      .setQuery(
        QueryBuilders.filteredQuery(
          QueryBuilders.regexpQuery("path", regEx),
          FilterBuilders.termFilter("tenant", tenant)))
      .execute()
      .actionGet()

    // if total hits exceeds maximum - abort right away returning empty array
    if (response.hits.totalHits() > indexConfiguration.maxPaths) {
      logger.debug("Total number of paths exceeds the limit: " + response.hits.totalHits())
      throw TooMuchDataExpectedException(
        "Total number of paths exceeds the limit: "
          + response.hits.totalHits()
          + " (the limit is "
          + indexConfiguration.maxPaths
          + ")")
    }

    val paths = ArrayList<String>()
    while (response.hits.hits.size > 0) {
      for (hit in response.hits) {
        paths.add(hit.sourceAsString)
      }
      response = client
        .prepareSearchScroll(response.scrollId)
        .setScroll(TimeValue(indexConfiguration.timeout.toLong()))
        .execute()
        .actionGet()
    }

    return "[" + joiner.join(paths) + "]"
  }

  fun getSearchPathsAsString(tenant: String, regEx: String, limit: Int): String {
    val response = client
      .prepareSearch(indexConfiguration.index)
      .setScroll(TimeValue(indexConfiguration.timeout.toLong()))
      .setSize(limit)
      .setQuery(
        QueryBuilders.filteredQuery(
          QueryBuilders.regexpQuery("path", regEx),
          FilterBuilders.termFilter("tenant", tenant)))
      .addField("path")
      .execute()
      .actionGet()

    val paths = ArrayList<String>()
    for (hit in response.hits) {
      paths.add(hit.field("path").getValue<Any>() as String)
    }

    return Joiner.on(",").skipNulls().join(paths)
  }

  @PreDestroy
  fun shutdown() {
    logger.info("Shutting down index service")
    client.close()
  }

  @Throws(TooMuchDataExpectedException::class)
  fun getPathsWithStats(tenant: String, wildcard: String): String {
    val regEx = WildcardUtil.getPathsRegExFromWildcard(wildcard)

    var response = client
      .prepareSearch(indexConfiguration.index)
      .setScroll(TimeValue(indexConfiguration.timeout.toLong()))
      .setSize(indexConfiguration.scroll)
      .setQuery(
        QueryBuilders.filteredQuery(
          QueryBuilders.regexpQuery("path", regEx),
          FilterBuilders.termFilter("tenant", tenant)))
      .addField("path")
      .execute()
      .actionGet()

    // if total hits exceeds maximum - abort right away returning empty array
    if (response.hits.totalHits() > indexConfiguration.maxPaths) {
      logger.debug("Total number of paths exceeds the limit: " + response.hits.totalHits())
      throw TooMuchDataExpectedException(
        "Total number of paths exceeds the limit: "
          + response.hits.totalHits()
          + " (the limit is "
          + indexConfiguration.maxPaths
          + ")")
    }

    val paths = ArrayList<String>()
    while (response.hits.hits.size > 0) {
      for (hit in response.hits) {
        paths.add(String(hit.field("path").getValue<CharArray>()))
      }
      response = client
        .prepareSearchScroll(response.scrollId)
        .setScroll(TimeValue(indexConfiguration.timeout.toLong()))
        .execute()
        .actionGet()
    }

    Collections.sort(paths)

    // we got the paths. Now let's get the counts
    val result = ArrayList<String>()
    for (path in paths) {
      val countResponse = client
        .prepareCount(indexConfiguration.index)
        .setQuery(
          QueryBuilders.filteredQuery(
            QueryBuilders.regexpQuery("path", "$path\\..*"),
            FilterBuilders.boolFilter()
              .must(FilterBuilders.termFilter("tenant", tenant))
              .must(FilterBuilders.termFilter("leaf", true))))
        .execute()
        .actionGet()
      val count = countResponse.count
      result.add("{\"path\": \"" + path + "\",\"count\":" + countResponse.count + "}")
    }

    return "[" + joiner.join(result) + "]"
  }

  companion object {
    internal val logger = LoggerFactory.getLogger(ElasticsearchIndexService::class.java)
  }
}
