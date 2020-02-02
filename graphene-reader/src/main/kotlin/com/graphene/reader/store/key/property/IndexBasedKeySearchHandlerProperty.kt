package com.graphene.reader.store.key.property

import com.graphene.common.rule.GrapheneRules
import com.graphene.reader.store.key.selector.KeySelectorProperty
import java.util.ArrayList

class IndexBasedKeySearchHandlerProperty : IndexProperty {

  var keySelectorProperty: KeySelectorProperty = KeySelectorProperty()
  var clusterName: String? = null
  var index: String? = null
  var tenant: String = GrapheneRules.DEFAULT_TENANT
  var type: String? = null
  var cluster: List<String> = ArrayList()
  var port: Int = 0
  var scroll: Int = 0
  var timeout: Int = 0
  var maxPaths: Int = 0

  override fun keySelectorProperty(): KeySelectorProperty = keySelectorProperty

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
    return "IndexBasedKeySearchHandler{" +
      "keySelectorProperty=$keySelectorProperty"
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
