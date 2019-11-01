package com.graphene.writer.store.key

import org.elasticsearch.action.admin.indices.get.GetIndexResponse
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.action.get.MultiGetResponse
import org.elasticsearch.client.RequestOptions
import java.io.Closeable

interface ElasticsearchClient : Closeable {

  fun createIndexIfNotExists(index: String, tenant: String)

  fun createTemplateIfNotExists(templatePattern: String, templateName: String, templateSource: String)

  fun getIndexWithDate(index: String, tenant: String): String

  fun getLatestIndex(index: String, tenant: String): String

  fun bulk(index: String, type: String, tenant: String, grapheneIndexRequests: List<GrapheneIndexRequest>, default: RequestOptions): BulkResponse

  fun mget(multiGetRequest: MultiGetRequest, default: RequestOptions): MultiGetResponse

  fun getIndices(): GetIndexResponse

  fun existsAlias(index: String, currentAlias: String): Boolean

}
