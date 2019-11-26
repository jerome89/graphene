package com.graphene.reader.store

import java.util.ArrayList
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Andrei Ivanov
 * @author Dark
 *
 * @since 1.0.0
 */
@ConfigurationProperties("graphene.reader.store.key.handlers.simple-key-search-handler")
class IndexProperty {
  var clusterName: String? = null
  var index: String? = null
  var tenant: String = "NONE"
  var type: String? = null
  var cluster: List<String> = ArrayList()
  var port: Int = 0
  var scroll: Int = 0
  var timeout: Int = 0
  var maxPaths: Int = 0

  override fun toString(): String {
    return "IndexProperty{" +
      "clusterName=$clusterName" +
      ", index=$index" +
      ", type=$type" +
      ", tenant=$tenant" +
      ", cluster=$cluster" +
      ", port=$port" +
      ", scroll=$scroll" +
      ", timeout=$timeout" +
      ", maxPaths=$maxPaths" +
      '}'
  }
}
