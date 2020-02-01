package com.graphene.reader.store.key.elasticsearch.property

import java.util.ArrayList

class SimpleKeySearchHandlerProperty : IndexProperty {
  var clusterName: String? = null
  var index: String? = null
  var tenant: String = "NONE"
  var type: String? = null
  var cluster: List<String> = ArrayList()
  var port: Int = 0
  var scroll: Int = 0
  var timeout: Int = 0
  var maxPaths: Int = 0

  override fun clusterName(): String? = clusterName

  override fun index(): String? = index

  override fun tenant(): String = tenant

  override fun type(): String? = type

  override fun cluster(): List<String> = cluster

  override fun port(): Int = port

  override fun scroll(): Int = scroll

  override fun timeout(): Int = timeout

  override fun maxPaths(): Int = maxPaths

  override fun toString(): String {
    return "SimpleKeySearchHandler{" +
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
