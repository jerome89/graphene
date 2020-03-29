package com.graphene.writer.store.key.elasticsearch

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

  fun createElasticsearchClient(rotationProperty: RotationProperty, cluster: List<String>, port: Int, protocol: String): ElasticsearchClient {
    val elasticsearchClientImpl = ElasticsearchClientTemplate(httpHosts(cluster, port, protocol), rotationProperty)

    elasticsearchClients.add(elasticsearchClientImpl)

    return elasticsearchClientImpl
  }

  private fun httpHosts(cluster: List<String>, port: Int, protocol: String): Array<HttpHost> {
    val httpHosts = mutableListOf<HttpHost>()
    for (index in cluster.indices) {
      httpHosts.add(HttpHost(cluster[index], port, protocol))
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
