package com.graphene.reader.service.index

import net.iponweb.disthene.reader.config.IndexConfiguration
import net.iponweb.disthene.reader.exceptions.TooMuchDataExpectedException
import org.elasticsearch.action.search.SearchType
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHits
import org.springframework.stereotype.Component

@Component
class ElasticsearchClient(
  private val client: TransportClient,
  private val indexConfiguration: IndexConfiguration
) {

  @Throws(TooMuchDataExpectedException::class)
  fun actionGet(regexQuery: String): Response {
    val searchResponse = client.prepareSearch(indexConfiguration.index)
      .setTypes(indexConfiguration.type)
      .setSearchType(SearchType.SCAN)
      .setScroll(TimeValue.timeValueMinutes(1))
      .setSize(1000)
      .setQuery(QueryBuilders.regexpQuery(indexConfiguration.type, regexQuery))
      .execute()
      .actionGet()

    val response = client.prepareSearchScroll(searchResponse.scrollId)
      .setScroll(TimeValue.timeValueMinutes(1))
      .execute()
      .actionGet()

    // if total hits exceeds maximum - abort right away returning empty array
    if (response.hits.totalHits() > indexConfiguration.maxPaths) {
      ElasticsearchIndexService.logger.debug("Total number of paths exceeds the limit: " + response.hits.totalHits())
      throw TooMuchDataExpectedException(
        "Total number of paths exceeds the limit: "
          + response.hits.totalHits()
          + " (the limit is "
          + indexConfiguration.maxPaths
          + ")")
    }

    return Response(
      response.scrollId,
      response.hits,
      response.hits.hits.size
    )
  }

  fun searchScroll(response: Response): Response {
    val response = client.prepareSearchScroll(response.scrollId)
      .setScroll(TimeValue.timeValueMinutes(1))
      .execute()
      .actionGet()

    return Response(
      response.scrollId,
      response.hits,
      response.hits.hits.size
    )
  }

  data class Response(
    val scrollId: String,
    val hits: SearchHits,
    val size: Int
  )

}
