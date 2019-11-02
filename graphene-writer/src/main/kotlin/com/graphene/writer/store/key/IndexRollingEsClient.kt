package com.graphene.writer.store.key

import com.graphene.common.key.RotationProperty
import com.graphene.common.key.RotationStrategy
import org.elasticsearch.action.admin.indices.get.GetIndexResponse
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.action.get.MultiGetResponse
import org.elasticsearch.client.RequestOptions

class IndexRollingEsClient(
  private val elasticsearchClient: ElasticsearchClient,
  rotationProperty: RotationProperty
) : ElasticsearchClient {

  var rotationStrategy = RotationStrategy.of(rotationProperty)

  override fun createTemplateIfNotExists(templatePattern: String, templateName: String, templateSource: String) {
    elasticsearchClient.createTemplateIfNotExists(templatePattern, templateName, templateSource)
  }

  override fun getLatestIndex(index: String, tenant: String): String {
    val response = getIndices()
    var latestIndexPosition = 0
    var latestIndex = getIndexWithDate(index, tenant)
    for (responseIndex in response.indices) {
      val indexPatterns = responseIndex.split("_")
      val indexName = indexPatterns[0]

      if (responseIndex == indexName && latestIndexPosition < indexPatterns[2].toInt()) {
        latestIndexPosition = indexPatterns[2].toInt()
        latestIndex = responseIndex
      }
    }
    return latestIndex
  }

  override fun getIndices(): GetIndexResponse {
    return elasticsearchClient.getIndices()
  }

  override fun bulk(index: String, type: String, tenant: String, grapheneIndexRequests: List<GrapheneIndexRequest>, default: RequestOptions): BulkResponse {
    return elasticsearchClient.bulk(getIndexWithDate(index, tenant), type, tenant, grapheneIndexRequests, default)
  }

  override fun mget(multiGetRequest: MultiGetRequest, default: RequestOptions): MultiGetResponse {
    return elasticsearchClient.mget(multiGetRequest, default)
  }

  override fun close() {
    elasticsearchClient.close()
  }

  override fun createIndexIfNotExists(index: String, tenant: String) {
    elasticsearchClient.createIndexIfNotExists(getIndexWithDate(index, tenant), tenant)
  }

  override fun existsAlias(index: String, currentAlias: String): Boolean {
    return elasticsearchClient.existsAlias(index, currentAlias)
  }

  override fun getIndexWithDate(index: String, tenant: String): String {
    return "${index}_${tenant}_${rotationStrategy.getDate()}"
  }
}
