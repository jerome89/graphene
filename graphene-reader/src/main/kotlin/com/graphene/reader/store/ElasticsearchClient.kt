package com.graphene.reader.store

import com.graphene.reader.store.key.selector.KeySelector
import javax.annotation.PreDestroy
import net.iponweb.disthene.reader.exceptions.TooMuchDataExpectedException
import org.elasticsearch.action.search.ClearScrollRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.search.SearchScrollRequest
import org.elasticsearch.action.support.IndicesOptions
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.search.SearchHits
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ElasticsearchClient(
  private val client: RestHighLevelClient,
  private val indexProperty: IndexProperty,
  private val keySelector: KeySelector
) {

  @Throws(TooMuchDataExpectedException::class)
  fun query(query: QueryBuilder, from: Long, to: Long): Response {
    val searchSourceBuilder = SearchSourceBuilder()
    searchSourceBuilder.query(query)
    searchSourceBuilder.size(indexProperty.scroll)

    val selectedIndex = keySelector.select(indexProperty.index!!, indexProperty.tenant, from, to)
    val searchRequest = SearchRequest(*selectedIndex.toTypedArray())
    searchRequest.source(searchSourceBuilder)
    searchRequest.scroll(TimeValue(indexProperty.timeout.toLong()))
    searchRequest.indicesOptions(IndicesOptions.fromOptions(true, true, true, false))

    val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
    throwIfExceededMaxPaths(searchResponse)

    return Response.of(searchResponse)
  }

  fun searchScroll(response: Response): Response {
    val scrollRequest = SearchScrollRequest(response.scrollId)
    scrollRequest.scroll(TimeValue.timeValueMillis(indexProperty.timeout.toLong()))

    val searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT)
    return Response.of(searchResponse)
  }

  fun clearScroll(scrollIds: List<String>) {
    val clearScrollRequest = ClearScrollRequest()
    clearScrollRequest.scrollIds(scrollIds)

    val clearScroll = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT)
    if (!clearScroll.isSucceeded) {
      logger.warn("[$scrollIds] scroll clears is failed.")
    }
  }

  private fun throwIfExceededMaxPaths(response: SearchResponse) {
    // if total hits exceeds maximum - abort right away returning empty array
    if (response.hits.totalHits > indexProperty.maxPaths) {
      logger.debug("Total number map paths exceeds the limit: " + response.hits.totalHits)
      throw TooMuchDataExpectedException(
        "Total number map paths exceeds the limit: " +
          response.hits.totalHits +
          " (the limit is " +
          indexProperty.maxPaths +
          ")")
    }
  }

  @PreDestroy
  fun shutdown() {
    logger.info("Shutting down elasticsearch client")
    client.close()
  }

  data class Response private constructor(
    val scrollId: String,
    val hits: SearchHits,
    val size: Int
  ) {
    companion object {
      fun of(response: SearchResponse): Response {
        return Response(
          response.scrollId,
          response.hits,
          response.hits.hits.size
        )
      }
    }
  }

  companion object {
    internal val logger = LoggerFactory.getLogger(ElasticsearchClient::class.java)
  }
}
