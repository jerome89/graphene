package com.graphene.writer.store.key.property

import com.graphene.writer.config.IndexBulkConfiguration
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory

class ElasticsearchKeyStoreHandlerProperty(
  var enabled: Boolean = false,
  var isCache: Boolean = false,
  var expire: Long = 0,
  var clusterName: String = "graphene",
  var templateIndexPattern: String = "metric*",
  var index: String = "metric",
  var type: String = "path",
  var cluster: String = "127.0.0.1",
  var port: Int = 443,
  var protocol: String = "https",
  var bulk: IndexBulkConfiguration? = null
) {

  @PostConstruct
  fun init() {
    logger.info("Load Graphene ElasticsearchKeyStoreHandlerProperty : {}", toString())
  }

  override fun toString(): String {
    return "ElasticsearchKeyStoreHandlerProperty(enabled=$enabled, isCache=$isCache, expire=$expire, clusterName='$clusterName', index='$index', type='$type', cluster=$cluster, port=$port, protocol=$protocol, bulk=$bulk)"
  }

  companion object {
    private val logger = LoggerFactory.getLogger(ElasticsearchKeyStoreHandlerProperty::class.java)
  }
}
