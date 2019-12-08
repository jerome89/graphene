package com.graphene.reader.store.key.optimizer

import org.elasticsearch.index.query.QueryBuilder

interface ElasticsearchQueryOptimizer {

  fun optimizeBranchQuery(pathExpression: String): QueryBuilder

  fun optimizeLeafQuery(pathExpression: String): QueryBuilder
}
