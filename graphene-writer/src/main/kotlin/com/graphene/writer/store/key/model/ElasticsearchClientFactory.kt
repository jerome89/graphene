package com.graphene.writer.store.key.model

import org.apache.http.HttpHost
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Component
class ElasticsearchClientFactory {

  lateinit var elasticsearchClients: MutableList<ElasticsearchClient>

  @PostConstruct
  fun init(): Unit {
    elasticsearchClients = mutableListOf()
  }

  fun createElasticsearchClient(cluster: List<String>): ElasticsearchClient {
    val elasticsearchClientImpl = ElasticsearchClientImpl(httpHosts(cluster))
    elasticsearchClientImpl.init()

    elasticsearchClients.add(elasticsearchClientImpl)

    return elasticsearchClientImpl
  }

  fun createIndexRollingEsClient(cluster: List<String>): ElasticsearchClient {
    return IndexRollingDecorator(createElasticsearchClient(cluster))
  }

  private fun httpHosts(cluster: List<String>): Array<HttpHost> {
    val httpHosts = mutableListOf<HttpHost>()
    for (index in cluster.indices) {
      httpHosts.add(HttpHost(cluster[index], 9200, "http"))
    }
    return httpHosts.toTypedArray()
  }

  @PreDestroy
  fun destroy() {
    for (elasticsearchClient in elasticsearchClients) {
      elasticsearchClient.close()
    }
  }
}
