package com.graphene.writer.store.key.model

import com.graphene.writer.config.IndexBulkConfiguration
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "graphene.writer.store.key.handlers.index-based-key-store-handler")
data class IndexBasedKeyStoreHandlerProperty(
  var enabled: Boolean = false,
  var clusterName: String? = null,
  var index: String? = null,
  var type: String? = null,
  var isCache: Boolean = false,
  var expire: Long = 0,
  var cluster: List<String> = mutableListOf(),
  var port: Int = 0,
  var bulk: IndexBulkConfiguration? = null
) {

  @PostConstruct
  fun init() {
    logger.info("Load Graphene IndexBasedKeyStoreHandlerProperty : {}", toString())
  }

  companion object {
    private val logger = LoggerFactory.getLogger(IndexBasedKeyStoreHandlerProperty::class.java)
  }

}
