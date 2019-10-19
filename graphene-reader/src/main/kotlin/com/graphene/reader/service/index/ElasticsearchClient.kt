package com.graphene.reader.service.index

import com.graphene.reader.service.index.model.IndexProperty
import net.iponweb.disthene.reader.exceptions.TooMuchDataExpectedException
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.search.SearchHits
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.annotation.PreDestroy
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchScrollRequest

@Component
class ElasticsearchClient(
  private val client: RestHighLevelClient,
  private val indexProperty: IndexProperty
) {

  @Throws(TooMuchDataExpectedException::class)
  fun query(query: QueryBuilder): Response {
    val searchSourceBuilder = SearchSourceBuilder()
    searchSourceBuilder.query(query)
    searchSourceBuilder.size(indexProperty.scroll)

    val searchRequest = SearchRequest(indexProperty.index)
    searchRequest.source(searchSourceBuilder)
    searchRequest.scroll(TimeValue(indexProperty.timeout.toLong()))

    val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
    throwIfExceededMaxPaths(searchResponse)

    return Response.of(searchResponse)
  }

  fun searchScroll(response: Response): Response {
    val scrollRequest = SearchScrollRequest(response.scrollId)
    scrollRequest.scroll(TimeValue.timeValueSeconds(indexProperty.timeout.toLong()))

    val searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT)
    return Response.of(searchResponse)
  }

  private fun throwIfExceededMaxPaths(response: SearchResponse) {
    // if total hits exceeds maximum - abort right away returning empty array
    if (response.hits.totalHits > indexProperty.maxPaths) {
      logger.debug("Total number map paths exceeds the limit: " + response.hits.totalHits)
      throw TooMuchDataExpectedException(
        "Total number map paths exceeds the limit: "
          + response.hits.totalHits
          + " (the limit is "
          + indexProperty.maxPaths
          + ")")
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
