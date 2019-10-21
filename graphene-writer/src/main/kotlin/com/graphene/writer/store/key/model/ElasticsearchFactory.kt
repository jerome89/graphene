package com.graphene.writer.store.key.model

import org.apache.http.HttpHost
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.apache.log4j.Logger
import org.elasticsearch.client.NodeSelector
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.sniff.Sniffer
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Component
class ElasticsearchFactory(
  private val property: SimpleKeyStoreHandlerProperty
) {

  private val logger = Logger.getLogger(ElasticsearchFactory::class.java)

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

  private fun httpHosts(): Array<HttpHost> {
    val httpHosts = mutableListOf<HttpHost>()
    for (index in property.cluster.indices) {
      httpHosts.add(HttpHost(property.cluster[index], 9200, "http"))
    }
    return httpHosts.toTypedArray()
  }

  fun restHighLevelClient(): RestHighLevelClient {
    return restHighLevelClient
  }

  @PreDestroy
  fun destroy() {
    logger.info("Closing ES client")
    restHighLevelClient.close()
    sniffer.close()
    logger.info("Closed ES client")
  }
}
