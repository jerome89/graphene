package com.graphene.reader.store.key.handler

import com.graphene.reader.store.key.property.IndexProperty
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import org.apache.http.HttpHost
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.elasticsearch.client.NodeSelector
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.sniff.Sniffer
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class ElasticsearchFactory(
  val indexProperty: IndexProperty
) {

  private lateinit var restHighLevelClient: RestHighLevelClient
  private lateinit var sniffer: Sniffer

  @PostConstruct
  fun init() {
    val restClientBuilder = RestClient.builder(*httpHosts())
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

  @Bean
  fun restHighLevelClient(): RestHighLevelClient = restHighLevelClient

  @Bean
  fun sniffer(restHighLevelClient: RestHighLevelClient): Sniffer = sniffer

  private fun httpHosts(): Array<HttpHost> {
    val httpHosts = mutableListOf<HttpHost>()
    for (index in indexProperty.cluster().indices) {
      httpHosts.add(HttpHost(indexProperty.cluster()[index], 9200, "http"))
    }
    return httpHosts.toTypedArray()
  }

  @PreDestroy
  fun destroy() {
    restHighLevelClient.close()
    sniffer.close()
  }
}
