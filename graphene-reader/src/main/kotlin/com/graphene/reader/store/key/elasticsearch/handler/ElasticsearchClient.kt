package com.graphene.reader.store.key.elasticsearch.handler

import com.graphene.reader.exceptions.TooMuchDataExpectedException
import com.graphene.reader.store.key.elasticsearch.property.IndexProperty
import com.graphene.reader.store.key.elasticsearch.selector.KeySelector
import java.util.Objects
import javax.annotation.PreDestroy
import org.apache.commons.lang3.StringUtils
import org.elasticsearch.action.search.ClearScrollRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.search.SearchScrollRequest
import org.elasticsearch.action.support.IndicesOptions
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.GetMappingsRequest
import org.elasticsearch.client.indices.GetMappingsResponse
import org.elasticsearch.cluster.metadata.MappingMetaData
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.search.SearchHits
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ElasticsearchClient(
  private val client: RestHighLevelClient,
  private val indexProperty: IndexProperty,
  private val keySelector: KeySelector
) {

  fun query(query: QueryBuilder, from: Long, to: Long, specifiedField: String = "", limit: Int = 100): Response {
    val searchSourceBuilder = SearchSourceBuilder()
    if (StringUtils.isNotBlank(specifiedField)) {
      searchSourceBuilder.fetchSource(arrayOf(specifiedField), null)
      searchSourceBuilder.aggregation(AggregationBuilders.terms(AGGREGATION_KEY).field(specifiedField).size(limit))
    }
    searchSourceBuilder.query(query)
    searchSourceBuilder.size(indexProperty.scroll())

    val selectedIndex = keySelector.select(indexProperty.index()!!, indexProperty.tenant(), from, to)
    val searchRequest = SearchRequest(*selectedIndex.toTypedArray())
    searchRequest.source(searchSourceBuilder)
    searchRequest.scroll(TimeValue(indexProperty.timeout().toLong()))
    searchRequest.indicesOptions(IndicesOptions.fromOptions(true, true, true, false))

    val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)

    return Response.of(searchResponse)
  }

  fun getFieldMapping(from: Long, to: Long): MappingsResponse {
    val getMappingsRequest = GetMappingsRequest()
    val selectedIndex = keySelector.select(indexProperty.index()!!, indexProperty.tenant(), from, to)
    getMappingsRequest.indices(*selectedIndex.toTypedArray())
    getMappingsRequest.indicesOptions(
      IndicesOptions.fromOptions(
        true, true, true, false
      )
    )
    val response = client.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT)

    return MappingsResponse.of(response)
  }

  fun searchScroll(response: Response): Response {
    val scrollRequest = SearchScrollRequest(response.scrollId)
    scrollRequest.scroll(TimeValue.timeValueMillis(indexProperty.timeout().toLong()))

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
    if (response.hits.totalHits > indexProperty.maxPaths()) {
      logger.debug("Total number map paths exceeds the limit: " + response.hits.totalHits)
      throw TooMuchDataExpectedException(
        "Total number map paths exceeds the limit: " +
          response.hits.totalHits +
          " (the limit is " +
          indexProperty.maxPaths() +
          ")")
    }
  }

  @PreDestroy
  fun shutdown() {
    logger.info("Shutting down elasticsearch client")
    client.close()
  }

  data class MappingsResponse(
    val mappings: MutableCollection<MappingMetaData>
  ) {
    companion object {
      fun of(response: GetMappingsResponse): MappingsResponse {
        return MappingsResponse(
                response.mappings().values
        )
      }
    }
  }

  data class Response(
    val scrollId: String,
    val hits: SearchHits,
    val size: Int,
    val tagValues: Set<String>
  ) {
    companion object {
      fun of(response: SearchResponse): Response {
        val tagValues = mutableSetOf<String>()
        if (Objects.nonNull(response.aggregations) &&
          Objects.nonNull(response.aggregations.asMap[AGGREGATION_KEY]) &&
          ! (response.aggregations.asMap[AGGREGATION_KEY] as ParsedStringTerms).buckets.isNullOrEmpty()) {
          for (bucket in (response.aggregations.asMap[AGGREGATION_KEY] as ParsedStringTerms).buckets) {
            tagValues.add(bucket.keyAsString)
          }
        }
        return Response(
                response.scrollId,
                response.hits,
                response.hits.hits.size,
                tagValues
        )
      }
    }
  }

  companion object {
    internal const val AGGREGATION_KEY = "AGGREGATION"
    internal val logger = LoggerFactory.getLogger(ElasticsearchClient::class.java)
  }
}
