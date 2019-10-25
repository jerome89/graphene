package com.graphene.reader.service.index.model

import org.springframework.boot.context.properties.ConfigurationProperties

import java.util.ArrayList

/**
 * @author Andrei Ivanov
 * @author Dark
 *
 * @since 1.0.0
 */
@ConfigurationProperties("graphene.reader.store.key")
class IndexProperty {
  var name: String? = null
  var index: String? = null
  var type: String? = null
  var cluster: List<String> = ArrayList()
  var port: Int = 0
  var scroll: Int = 0
  var timeout: Int = 0
  var maxPaths: Int = 0

  override fun toString(): String {
    return "IndexProperty{" +
      "name=$name" +
      ", index=$index" +
      ", type=$type" +
      ", cluster=$cluster" +
      ", port=$port" +
      ", scroll=$scroll" +
      ", timeout=$timeout" +
      ", maxPaths=$maxPaths" +
      '}'
  }
}
