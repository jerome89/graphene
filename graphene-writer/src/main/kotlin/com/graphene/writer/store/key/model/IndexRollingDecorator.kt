package com.graphene.writer.store.key.model

import com.graphene.writer.store.key.rotator.SimpleKeyRotator
import org.apache.log4j.Logger
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient

class IndexRollingDecorator(
  elasticsearchClient: ElasticsearchClient
) : ElasticsearchClientDecorator(elasticsearchClient) {
  override fun createTemplateIfNotExists(templateName: String, templateSource: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun close() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createIndexIfNotExists(index: String) {
    elasticsearchClient.createIndexIfNotExists("$index.0")
  }

//  private val logger = Logger.getLogger(SimpleKeyRotator::class.java)
//  private var restHighLevelClient: RestHighLevelClient = elasticsearchClientImpl.restHighLevelClient()
//
//  override fun getCurrentPointer(): String {
//    return "${property.index}.${property.tenant}.CURRENT"
//  }

//  override fun run() {
//    var currentPointer = getCurrentPointer()
//    var dateAlias = "${property.index}.${property.tenant}.20191025"
//
//    if (existsAlias(property.index, currentPointer)) {
//      return
//    }
//
//    val aliasAction = IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
//      .index(elasticsearchClientImpl.getInitialIndex())
//      .aliases(currentPointer, dateAlias)
//
//    val indicesAliasesRequest = IndicesAliasesRequest()
//    indicesAliasesRequest.addAliasAction(aliasAction)
//    restHighLevelClient.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT)
//  }
//
//  private fun existsAlias(index: String, currentAlias: String): Boolean {
//    val alias = restHighLevelClient.indices().getAlias(GetAliasesRequest(currentAlias), RequestOptions.DEFAULT)
//    val aliases = alias.aliases
//
//    for (entry in aliases.entries) {
//      logger.debug("index : ${entry.key} / aliasMeta : ${entry.value}")
//
//      if (entry.key == index) {
//        return true
//      }
//    }
//
//    return false
//  }

}
