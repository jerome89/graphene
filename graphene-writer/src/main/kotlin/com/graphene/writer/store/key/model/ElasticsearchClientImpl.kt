package com.graphene.writer.store.key.model

import org.apache.http.HttpHost
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.apache.log4j.Logger
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.get.GetIndexRequest
import org.elasticsearch.action.admin.indices.get.GetIndexResponse
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

  override fun existsAlias(index: String, currentAlias: String): Boolean {
    val alias = restHighLevelClient.indices().getAlias(GetAliasesRequest(currentAlias), RequestOptions.DEFAULT)
    val aliases = alias.aliases

    for (entry in aliases.entries) {
      logger.debug("index : ${entry.key} / aliasMeta : ${entry.value}")

      if (entry.key == index) {
        return true
      }
    }

    return false
  }

  override fun addAlias(latestIndex: String, currentPointer: String, dateAlias: String) {
    val aliasAction = IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
      .index(latestIndex)
      .aliases(currentPointer, dateAlias)

    val indicesAliasesRequest = IndicesAliasesRequest()
    indicesAliasesRequest.addAliasAction(aliasAction)
    restHighLevelClient.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT)
  }

  override fun getCurrentIndex(index: String, tenant: String): String {
    return index
  }

  override fun mget(multiGetRequest: MultiGetRequest, default: RequestOptions): MultiGetResponse {
    return restHighLevelClient.mget(multiGetRequest, default)
  }

  override fun getIndices(): GetIndexResponse {
    val request = GetIndexRequest().indices("*")
    return restHighLevelClient.indices().get(request, RequestOptions.DEFAULT)
  }

  override fun bulk(index: String, type: String, tenant: String, grapheneIndexRequests: List<GrapheneIndexRequest>, default: RequestOptions): BulkResponse {
    val bulkRequest = BulkRequest()
    for (grapheneIndexRequest in grapheneIndexRequests) {
      val indexRequest = IndexRequest(getLatestIndex(index), type, grapheneIndexRequest.id)
      indexRequest.source(grapheneIndexRequest.source)
      bulkRequest.add(indexRequest)
    }
    return restHighLevelClient.bulk(bulkRequest, default)
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
    return index
  }

  override fun close() {
    logger.info("Closing ES client")
    restHighLevelClient.close()
    sniffer.close()
    logger.info("Closed ES client")
  }

}
