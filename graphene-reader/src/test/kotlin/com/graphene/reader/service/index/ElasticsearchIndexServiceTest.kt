package com.graphene.reader.service.index

import com.graphene.reader.store.ElasticsearchClient
import com.graphene.reader.store.key.SimpleKeySearchHandler
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import kotlin.test.assertEquals
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.search.SearchResponseSections
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.SearchHits
import org.elasticsearch.search.aggregations.Aggregations
import org.elasticsearch.search.profile.SearchProfileShardResults
import org.elasticsearch.search.suggest.Suggest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong

internal class ElasticsearchIndexServiceTest {

  private lateinit var simpleKeySearchHandler: SimpleKeySearchHandler

  private val elasticsearchClient: ElasticsearchClient = mockk()

  @BeforeEach
  internal fun setUp() {
    simpleKeySearchHandler = SimpleKeySearchHandler(elasticsearchClient)
  }

  @Test
  internal fun `should return deduplicated hierarchy metric path`() {
    // given
    val response = ElasticsearchClient.Response.of(
      searchResponse(
        arrayOf(
          internalSearchHit(Pair("path", "hosts.i-a.cpu.usage")),
          internalSearchHit(Pair("path", "hosts.i-b.cpu.usage")),
          internalSearchHit(Pair("path", "hosts.i-c.cpu.usage"))))
    )

    every { elasticsearchClient.query(any(), any(), any()) } answers { response }
    every { elasticsearchClient.searchScroll(any()) } answers { emptyResponse() }

    // when
    val hierarchyMetricPath = simpleKeySearchHandler.getHierarchyMetricPaths("NONE", "hosts.*.cpu.*", anyLong(), anyLong())

    // then
    assertEquals(1, hierarchyMetricPath.size)
  }

  private fun emptyResponse() = ElasticsearchClient.Response.of(searchResponse(emptyArray()))

  private fun searchResponse(internalSearchHits: Array<SearchHit>): SearchResponse {
    val internalSearchResponse = SearchResponseSections(
      SearchHits(internalSearchHits, internalSearchHits.size.toLong(), 0.0f),
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

  private fun internalSearchHit(pathPair: Pair<String, Any>): SearchHit {
    val internalSearchHit = mockk<SearchHit>()
    every { internalSearchHit.sourceAsMap } answers { mapOf(Pair("@tenant", "NONE"), Pair("depth", 4), Pair("leaf", true), pathPair) }
    return internalSearchHit
  }
}
