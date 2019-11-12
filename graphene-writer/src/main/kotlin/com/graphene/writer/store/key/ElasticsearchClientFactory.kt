package com.graphene.writer.store.key

import com.graphene.common.key.RotationProperty
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import org.apache.http.HttpHost
import org.springframework.stereotype.Component

@Component
class ElasticsearchClientFactory {

  lateinit var elasticsearchClients: MutableList<ElasticsearchClient>

  @PostConstruct
  fun init() {
    elasticsearchClients = mutableListOf()
  }

  fun createElasticsearchClient(rotationProperty: RotationProperty, cluster: List<String>): ElasticsearchClient {
    val elasticsearchClientImpl = ElasticsearchClientTemplate(httpHosts(cluster), rotationProperty)

    elasticsearchClients.add(elasticsearchClientImpl)

    return elasticsearchClientImpl
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
