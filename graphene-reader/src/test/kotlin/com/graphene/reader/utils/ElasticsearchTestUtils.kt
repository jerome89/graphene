package com.graphene.reader.utils

import com.graphene.reader.store.key.elasticsearch.handler.ElasticsearchClient
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.search.SearchResponseSections
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.SearchHits
import org.elasticsearch.search.aggregations.Aggregations
import org.elasticsearch.search.profile.SearchProfileShardResults
import org.elasticsearch.search.suggest.Suggest

class ElasticsearchTestUtils {

  companion object {
    fun searchResponse(internalSearchHitsList: Array<Array<Pair<String, Any>>>): SearchResponse {
      val hits = mutableListOf<SearchHit>()

      for (internalSearchHits in internalSearchHitsList) {
        hits.add(convertSearchHit(internalSearchHits))
      }

      val internalSearchResponse = SearchResponseSections(
        SearchHits(hits.toTypedArray(), internalSearchHitsList.size.toLong(), 0.0f),
        Aggregations(emptyList()),
        Suggest(listOf()),
        false,
        false,
        SearchProfileShardResults(mapOf()),
        0
      )

      return SearchResponse(
        internalSearchResponse,
        UUID.randomUUID().toString(),
        10,
        10,
        0,
        1000,
        null,
        null
      )
    }

    fun emptyResponse() = ElasticsearchClient.Response.of(searchResponse(emptyArray()))

    private fun convertSearchHit(sourceAsMap: Array<Pair<String, Any>>): SearchHit {
      val internalSearchHit = mockk<SearchHit>()
      every { internalSearchHit.sourceAsMap } answers { mapOf(*sourceAsMap) }
      return internalSearchHit
    }
  }
}
