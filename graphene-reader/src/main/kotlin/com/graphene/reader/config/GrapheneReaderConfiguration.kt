package com.graphene.reader.config

import net.iponweb.disthene.reader.config.IndexConfiguration
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.context.annotation.Bean

@Configuration
class GrapheneReaderConfiguration {

  @Bean
  fun corsConfigurer(): WebMvcConfigurer {
    return object : WebMvcConfigurer {
      override fun addCorsMappings(registry: CorsRegistry?) {
        registry!!.addMapping("/**")
      }
    }
  }

  @Bean
  fun transportClient(
    indexConfiguration: IndexConfiguration
  ): TransportClient {

    val settings = ImmutableSettings.settingsBuilder()
      .put("cluster.name", indexConfiguration.name)
      .put("client.transport.sniff", true)
      .put("client.transport.ping_timeout", "5s")
      .put("client.transport.nodes_sampler_interval", "5s")
      .build()
    val client = TransportClient(settings)

    for (node in indexConfiguration.cluster) {
      client.addTransportAddress(InetSocketTransportAddress(node, indexConfiguration.port))
    }

    return client
  }
}
