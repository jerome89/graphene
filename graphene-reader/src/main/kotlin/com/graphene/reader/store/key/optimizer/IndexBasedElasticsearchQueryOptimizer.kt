package com.graphene.reader.store.key.optimizer

import com.google.common.base.Joiner
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.stereotype.Component

@Component
class IndexBasedElasticsearchQueryOptimizer : ElasticsearchQueryOptimizer {

  override fun optimize(pathExpression: String): QueryBuilder {
    val indexBasedPath = pathExpression.split(".")

    val boolQuery = QueryBuilders.boolQuery()

    for (withIndex in indexBasedPath.withIndex()) {
      val termQuery = QueryBuilders.termQuery(withIndex.index.toString(), withIndex.value)

      boolQuery.filter(
        termQuery
      )
    }

    return boolQuery
  }

  override fun optimize(pathExpressions: MutableList<String>): QueryBuilder {
    return QueryBuilders.regexpQuery("_id", Joiner.on("|").skipNulls().join(pathExpressions))
  }
}
