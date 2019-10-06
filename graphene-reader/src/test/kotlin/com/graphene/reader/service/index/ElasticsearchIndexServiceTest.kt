package com.graphene.reader.service.index

import io.mockk.every
import io.mockk.mockk
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.aggregations.InternalAggregations
import org.elasticsearch.search.facet.InternalFacets
import org.elasticsearch.search.internal.InternalSearchHit
import org.elasticsearch.search.internal.InternalSearchHits
import org.elasticsearch.search.internal.InternalSearchResponse
import org.elasticsearch.search.suggest.Suggest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class ElasticsearchIndexServiceTest {

  private lateinit var elasticsearchIndexService: ElasticsearchIndexService

  private val elasticsearchClient: ElasticsearchClient = mockk()

  @BeforeEach
  internal fun setUp() {
    elasticsearchIndexService = ElasticsearchIndexService(elasticsearchClient)
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

    every { elasticsearchClient.query(any()) } answers { response }
    every { elasticsearchClient.searchScroll(any()) } answers { emptyResponse() }

    // when
    val hierarchyMetricPath = elasticsearchIndexService.getHierarchyMetricPaths("NONE", "hosts.*.cpu.*")

    // then
    assertEquals(1, hierarchyMetricPath.size)
  }

  private fun emptyResponse() = ElasticsearchClient.Response.of(searchResponse(emptyArray()))

  private fun searchResponse(internalSearchHits: Array<InternalSearchHit>): SearchResponse {
    val internalSearchResponse = InternalSearchResponse(
      InternalSearchHits(internalSearchHits, internalSearchHits.size.toLong(), 0.0f),
      InternalFacets(emptyList()),
      InternalAggregations.EMPTY,
      Suggest(),
      false,
      false
    )

    return SearchResponse(
      internalSearchResponse,
      UUID.randomUUID().toString(),
      10,
      10,
      1000,
      emptyArray()
    )
  }

  private fun internalSearchHit(pathPair: Pair<String, Any>): InternalSearchHit {
    val internalSearchHit = mockk<InternalSearchHit>()
    every { internalSearchHit.sourceAsMap() } answers { mapOf(Pair("@tenant", "NONE"), Pair("depth", 4), Pair("leaf", true), pathPair) }
    return internalSearchHit
  }
}