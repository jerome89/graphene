package com.graphene.writer.store.key.property

import com.graphene.writer.config.IndexBulkConfiguration
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "graphene.writer.store.key.handlers.index-based-key-store-handler")
data class IndexBasedKeyStoreHandlerProperty(
  var enabled: Boolean = false,
  var isCache: Boolean = false,
  var expire: Long = 0,
  override var clusterName: String = "graphene",
  override var tenant: String = "NONE",
  override var templateIndexPattern: String = "metric*",
  override var index: String = "metric",
  override var type: String = "path",
  override var cluster: List<String> = mutableListOf(),
  override var port: Int = 0,
  override var bulk: IndexBulkConfiguration? = null
) : ElasticsearchKeyStoreHandlerProperty(clusterName, tenant, templateIndexPattern, index, type, cluster, port, bulk) {

  @PostConstruct
  fun init() {
    logger.info("Load Graphene IndexBasedKeyStoreHandlerProperty : {}", toString())
  }

  override fun toString(): String {
    return "IndexBasedKeyStoreHandlerProperty(enabled=$enabled, isCache=$isCache, expire=$expire, clusterName='$clusterName', index='$index', type='$type', cluster=$cluster, port=$port, bulk=$bulk)"
  }

  companion object {
    private val logger = LoggerFactory.getLogger(IndexBasedKeyStoreHandlerProperty::class.java)
  }
}
