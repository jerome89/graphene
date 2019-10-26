package com.graphene.writer.store.key.model

import org.elasticsearch.action.admin.indices.get.GetIndexResponse
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.action.get.MultiGetResponse
import org.elasticsearch.client.RequestOptions

class IndexRollingDecorator(
  elasticsearchClient: ElasticsearchClient
) : ElasticsearchClientDecorator(elasticsearchClient) {

  override fun createTemplateIfNotExists(templatePattern: String, templateName: String, templateSource: String) {
    elasticsearchClient.createTemplateIfNotExists(templatePattern, templateName, templateSource)
  }

  override fun getLatestIndex(index: String): String {
    val response = getIndices()
    var latestIndexPosition = 0
    var latestIndex = "$index.0"
    for (responseIndex in response.indices) {
      val indexNameAndPosition = responseIndex.split(".")
      val indexName = indexNameAndPosition[0]

      if (responseIndex == indexName && latestIndexPosition < indexNameAndPosition[1].toInt()) {
        latestIndexPosition = indexNameAndPosition[1].toInt()
        latestIndex = responseIndex
      }
    }
    return latestIndex
  }

  override fun getIndices(): GetIndexResponse {
    return elasticsearchClient.getIndices()
  }

  override fun bulk(index: String, type: String, grapheneIndexRequests: List<GrapheneIndexRequest>, default: RequestOptions): BulkResponse {
    return elasticsearchClient.bulk(index, type, grapheneIndexRequests, default)
  }

  override fun mget(multiGetRequest: MultiGetRequest, default: RequestOptions): MultiGetResponse {
    return elasticsearchClient.mget(multiGetRequest, default)
  }

  override fun close() {
    elasticsearchClient.close()
  }

  override fun createIndexIfNotExists(index: String) {
    elasticsearchClient.createIndexIfNotExists("$index.0")
  }

}
