package com.graphene.writer.store.key.model

import com.graphene.writer.store.key.GrapheneIndexRequest
import org.apache.http.HttpHost
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.apache.log4j.Logger
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.get.GetIndexRequest
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.action.get.MultiGetResponse
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.NodeSelector
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.sniff.Sniffer
import org.elasticsearch.common.xcontent.XContentType
import javax.annotation.PostConstruct

class ElasticsearchClientImpl(
  private val httpHosts: Array<HttpHost>
) : ElasticsearchClient {

  private lateinit var restHighLevelClient: RestHighLevelClient
  private lateinit var sniffer: Sniffer

  private val logger = Logger.getLogger(ElasticsearchClientImpl::class.java)

  @PostConstruct
  fun init() {
    val restClientBuilder = RestClient.builder(*httpHosts)
      .setRequestConfigCallback { config ->
        config.setConnectTimeout(5_000)
        config.setSocketTimeout(30_000)
      }
      .setHttpClientConfigCallback { config ->
        config.setDefaultIOReactorConfig(
          IOReactorConfig.custom()
            .setIoThreadCount(Runtime.getRuntime().availableProcessors())
            .build()
        )
      }
      .setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS)

    restHighLevelClient = RestHighLevelClient(restClientBuilder)

    sniffer = Sniffer.builder(restHighLevelClient.lowLevelClient)
      .setSniffIntervalMillis(30_000)
      .build()
  }

  fun restHighLevelClient(): RestHighLevelClient {
    return restHighLevelClient
  }

  override fun bulk(index: String, type: String, grapheneIndexRequests: List<GrapheneIndexRequest>, default: RequestOptions): BulkResponse {
    val bulkRequest = BulkRequest()
    for (grapheneIndexRequest in grapheneIndexRequests) {
      val indexRequest = IndexRequest(getLatestIndex(index), type, grapheneIndexRequest.id)
      indexRequest.source(grapheneIndexRequest.source)
      bulkRequest.add(indexRequest)
    }
    return restHighLevelClient.bulk(bulkRequest, default)
  }

  override fun mget(multiGetRequest: MultiGetRequest, default: RequestOptions): MultiGetResponse {
    return restHighLevelClient.mget(multiGetRequest, default)
  }

  override fun createIndexIfNotExists(index: String) {
    if (restHighLevelClient.indices().exists(GetIndexRequest().indices(index), RequestOptions.DEFAULT)) {
      return
    }

    val createIndexRequest = CreateIndexRequest(index)
    restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT)
  }

  override fun createTemplateIfNotExists(templateIndexPattern: String, templateName: String, templateSource: String) {
    val putIndexTemplateRequest = PutIndexTemplateRequest(templateName)
    putIndexTemplateRequest.patterns(listOf(templateIndexPattern))
    putIndexTemplateRequest.source(templateSource, XContentType.JSON)

    restHighLevelClient.indices().putTemplate(putIndexTemplateRequest, RequestOptions.DEFAULT)
  }

  override fun getLatestIndex(index: String): String {
    val request = GetIndexRequest().indices("*")
    val response = restHighLevelClient.indices().get(request, RequestOptions.DEFAULT)
    var latestIndexPosition = 0
    var latestIndex = "$index.0"
    for (index in response.indices) {
      val indexNameAndPosition = index.split("\\.")
      val indexName = indexNameAndPosition[0]

      if (index == indexName && latestIndexPosition < indexNameAndPosition[1].toInt()) {
        latestIndexPosition = indexNameAndPosition[1].toInt()
        latestIndex = index
      }
    }
    return latestIndex
  }

  override fun close() {
    logger.info("Closing ES client")
    restHighLevelClient.close()
    sniffer.close()
    logger.info("Closed ES client")
  }

  fun getInitialIndex(index: String): String {
    return "$index.0"
  }

  fun putTemplate(putIndexTemplateRequest: PutIndexTemplateRequest) {
    restHighLevelClient.indices().putTemplate(putIndexTemplateRequest, RequestOptions.DEFAULT)
  }
}
