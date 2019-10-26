package com.graphene.writer.store.key.rotator

import com.graphene.writer.store.key.model.ElasticsearchClientImpl
import com.graphene.writer.store.key.model.ElasticsearchKeyStoreHandlerProperty
import org.apache.log4j.Logger
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions
import org.elasticsearch.client.RestHighLevelClient

class SimpleKeyRotator(
  var property: ElasticsearchKeyStoreHandlerProperty,
  var elasticsearchClientImpl: ElasticsearchClientImpl
) : KeyRotator {

  private val logger = Logger.getLogger(SimpleKeyRotator::class.java)
  private var restHighLevelClient: RestHighLevelClient = elasticsearchClientImpl.restHighLevelClient()

  override fun getCurrentPointer(): String {
    return "${property.index}.${property.tenant}.CURRENT"
  }

  override fun run() {
    var currentPointer = getCurrentPointer()
    var dateAlias = "${property.index}.${property.tenant}.20191025"

    if (existsAlias(property.index, currentPointer)) {
      return
    }

    val aliasAction = AliasActions(AliasActions.Type.ADD)
      .index(elasticsearchClientImpl.getInitialIndex())
      .aliases(currentPointer, dateAlias)

    val indicesAliasesRequest = IndicesAliasesRequest()
    indicesAliasesRequest.addAliasAction(aliasAction)
    restHighLevelClient.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT)
  }

  private fun existsAlias(index: String, currentAlias: String): Boolean {
    val alias = restHighLevelClient.indices().getAlias(GetAliasesRequest(currentAlias), RequestOptions.DEFAULT)
    val aliases = alias.aliases

    for (entry in aliases.entries) {
      logger.debug("index : ${entry.key} / aliasMeta : ${entry.value}")

      if (entry.key == index) {
        return true
      }
    }

    return false
  }

}
