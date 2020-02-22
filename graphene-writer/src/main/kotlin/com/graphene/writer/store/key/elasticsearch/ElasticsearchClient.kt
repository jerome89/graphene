package com.graphene.writer.store.key.elasticsearch

import java.io.Closeable
import org.elasticsearch.action.admin.indices.get.GetIndexResponse
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.action.get.MultiGetResponse
import org.elasticsearch.client.RequestOptions

interface ElasticsearchClient : Closeable, RotatedIndexAware {

  fun createIndexIfNotExists(index: String, tenant: String, from: Long?, to: Long?)

  fun createNameIndexIfNotExists(index: String, tenant: String)

  fun createTemplateIfNotExists(templatePattern: String, templateName: String, templateSource: String)

  fun bulk(index: String, type: String, tenant: String, grapheneIndexRequests: List<GrapheneIndexRequest>, default: RequestOptions): BulkResponse

  fun bulkAsync(index: String, type: String, tenant: String, grapheneIndexRequests: List<GrapheneIndexRequest>, default: RequestOptions)

  fun bulkAsyncNames(index: String, type: String, tenant: String, grapheneIndexRequests: List<GrapheneIndexRequest>, default: RequestOptions)

  fun mget(multiGetRequest: MultiGetRequest, default: RequestOptions): MultiGetResponse

  fun getIndices(): GetIndexResponse

  fun existsAlias(index: String, currentAlias: String): Boolean
}

interface RotatedIndexAware {

  fun getIndexWithCurrentDate(index: String, tenant: String): String

  fun getIndexWithDate(index: String, tenant: String, timestampMillis: Long): String

  fun getRangeIndex(index: String, tenant: String, from: Long, to: Long): Set<String>
}
