package com.graphene.reader.store.key.optimizer

import com.graphene.common.utils.PathExpressionUtils
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.stereotype.Component

/**
 *
 * @author dark
 * @since 1.4.0
 */
@Component
class IndexBasedElasticsearchQueryOptimizer : ElasticsearchQueryOptimizer {

  override fun optimizeLeafQuery(pathExpression: String): QueryBuilder {
    val boolQueryBuilder = optimizeBranchQuery(pathExpression) as BoolQueryBuilder
    boolQueryBuilder.filter(QueryBuilders.termQuery("depth", graphiteIndexBasedPaths(pathExpression).size))
    return boolQueryBuilder
  }

  override fun optimizeBranchQuery(pathExpression: String): QueryBuilder {
    val indexBasedPaths = graphiteIndexBasedPaths(pathExpression)

    val boolQuery = QueryBuilders.boolQuery()

    for (indexedValue in indexBasedPaths.withIndex()) {
      if (indexedValue.value == "*") {
        continue
      }

      when {
        isPrefixTermQuery(indexedValue) -> boolQuery.filter(QueryBuilders.prefixQuery(indexedValue.index.toString(), indexedValue.value))
        isTermsQuery(indexedValue) -> boolQuery.filter(QueryBuilders.termsQuery(indexedValue.index.toString(), terms(indexedValue)))
        isRegexQuery(indexedValue) -> boolQuery.filter(QueryBuilders.regexpQuery(indexedValue.index.toString(), PathExpressionUtils.getEscapedPathExpression(indexedValue.value)))
        else -> boolQuery.filter(QueryBuilders.termQuery(indexedValue.index.toString(), indexedValue.value))
      }
    }

    return boolQuery
  }

  private fun graphiteIndexBasedPaths(pathExpression: String) = pathExpression.split(".")

  fun terms(indexedValue: IndexedValue<String>): List<String> {
    val result = mutableListOf<String>()
    var termBuilder = StringBuilder()

    for (char in indexedValue.value) {
      if (skip(char)) {
        continue
      }

      if (char == ',' || char == '}') {
        result.add(termBuilder.toString())
        termBuilder = StringBuilder()
        continue
      }

      termBuilder.append(char)
    }

    return result
  }

  private fun skip(char: Char) = char == '{' || char == ' '

  private fun isRegexQuery(indexedValue: IndexedValue<String>): Boolean {
    return !PathExpressionUtils.isPlainPath(indexedValue.value)
  }

  private fun isTermsQuery(indexedValue: IndexedValue<String>): Boolean {
    return PathExpressionUtils.hasOnlyOrCondition(indexedValue.value)
  }

  private fun isPrefixTermQuery(indexedValue: IndexedValue<String>) =
    PathExpressionUtils.isPlainPath(indexedValue.value.substring(0, indexedValue.value.length - 1)) && indexedValue.value.last() == '*'
}
