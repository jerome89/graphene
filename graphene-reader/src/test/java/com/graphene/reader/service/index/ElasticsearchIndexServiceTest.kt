package com.graphene.reader.service.index

import io.mockk.every
import io.mockk.mockk
import org.elasticsearch.search.internal.InternalSearchHit
import org.elasticsearch.search.internal.InternalSearchHits
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
    val response = response(arrayOf(
      internalSearchHit(Pair("path", "hosts.i-a.cpu.usage")),
      internalSearchHit(Pair("path", "hosts.i-b.cpu.usage")),
      internalSearchHit(Pair("path", "hosts.i-c.cpu.usage"))
    ))

    every { elasticsearchClient.regexpQuery(any()) } answers { response }
    every { elasticsearchClient.searchScroll(any()) } answers { response(arrayOf()) }

    // when
    val hierarchyMetricPath = elasticsearchIndexService.getHierarchyMetricPaths("NONE", "hosts.*.cpu.*")

    // then
    assertEquals(1, hierarchyMetricPath.size)
  }

  private fun response(hits: Array<InternalSearchHit>): ElasticsearchClient.Response {
    return ElasticsearchClient.Response(UUID.randomUUID().toString(), InternalSearchHits(
      hits,
      hits.size.toLong(),
      0.0f
    ), hits.size)
  }

  private fun internalSearchHit(pathPair: Pair<String, Any>): InternalSearchHit {
    val internalSearchHit = mockk<InternalSearchHit>()
    every { internalSearchHit.sourceAsMap() } answers { mapOf(Pair("@tenant", "NONE"), Pair("depth", 4), Pair("leaf", true), pathPair) }
    return internalSearchHit
  }
}