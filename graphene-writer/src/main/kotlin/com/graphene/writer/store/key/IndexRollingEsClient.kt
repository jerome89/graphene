package com.graphene.writer.store.key

import org.elasticsearch.action.admin.indices.get.GetIndexResponse
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.action.get.MultiGetResponse
import org.elasticsearch.client.RequestOptions

class IndexRollingEsClient(
  private val elasticsearchClient: ElasticsearchClient
) : ElasticsearchClient {

  override fun addAlias(latestIndex: String, currentPointer: String, dateAlias: String) {
    elasticsearchClient.addAlias(latestIndex, currentPointer, dateAlias)
  }

  override fun createTemplateIfNotExists(templatePattern: String, templateName: String, templateSource: String) {
    elasticsearchClient.createTemplateIfNotExists(templatePattern, templateName, templateSource)
  }

  override fun getCurrentIndex(index: String, tenant: String): String {
    return "${index}.${tenant}.CURRENT"
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

  override fun bulk(index: String, type: String, tenant: String, grapheneIndexRequests: List<GrapheneIndexRequest>, default: RequestOptions): BulkResponse {
    return elasticsearchClient.bulk(getCurrentIndex(index, tenant), type, tenant, grapheneIndexRequests, default)
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

  override fun existsAlias(index: String, currentAlias: String): Boolean {
    return elasticsearchClient.existsAlias(index, currentAlias)
  }

  fun attachCurrentAliasToLatestIndex(index: String, tenant: String) {
    var currentAlias = getCurrentIndex(index, tenant)
    var dateAlias = "${index}.${tenant}.20191026"

    if (elasticsearchClient.existsAlias(index, currentAlias)) {
      return
    }

    elasticsearchClient.addAlias(getLatestIndex(index), currentAlias, dateAlias)
  }

}
