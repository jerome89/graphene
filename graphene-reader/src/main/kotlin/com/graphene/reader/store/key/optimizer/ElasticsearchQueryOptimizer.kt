package com.graphene.reader.store.key.optimizer

import org.elasticsearch.index.query.QueryBuilder

interface ElasticsearchQueryOptimizer {

  fun optimize(pathExpression: String): QueryBuilder

  fun optimize(pathExpressions: MutableList<String>): QueryBuilder
}
