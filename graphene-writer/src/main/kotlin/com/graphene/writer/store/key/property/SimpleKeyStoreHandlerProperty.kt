package com.graphene.writer.store.key.property

import com.graphene.writer.config.IndexBulkConfiguration
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "graphene.writer.store.key.handlers.simple-key-store-handler")
class SimpleKeyStoreHandlerProperty(
  var enabled: Boolean = false,
  var isCache: Boolean = false,
  var expire: Long = 0,
  override var clusterName: String = "graphene",
  override var tenant: String = "NONE",
  override var templateIndexPattern: String = "metric*",
  override var index: String = "metric",
  override var type: String = "path",
  override var cluster: List<String> = mutableListOf(),
  override var port: Int = 9300,
  override var bulk: IndexBulkConfiguration? = null
) : ElasticsearchKeyStoreHandlerProperty(clusterName, tenant, templateIndexPattern, index, type, cluster, port, bulk) {

  @PostConstruct
  fun init() {
    logger.info("Load Graphene SimpleKeyStoreHandlerProperty : {}", toString())
  }

  override fun toString(): String {
    return "SimpleKeyStoreHandlerProperty(enabled=$enabled, isCache=$isCache, expire=$expire, clusterName='$clusterName', index='$index', type='$type', cluster=$cluster, port=$port, bulk=$bulk)"
  }

  companion object {
    private val logger = LoggerFactory.getLogger(SimpleKeyStoreHandlerProperty::class.java)
  }

}
