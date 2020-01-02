package com.graphene.reader.store.tag.optimizer

import org.apache.commons.lang3.StringUtils
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.stereotype.Component

@Component
class ElasticsearchIntegratedTagSearchQueryOptimizer : ElasticsearchTagSearchQueryOptimizer {

  override fun optimize(tagSearchTarget: TagSearchTarget): QueryBuilder {
    val boolQuery = QueryBuilders.boolQuery()
    addSearchTargetFilter(boolQuery, tagSearchTarget)
    addQueriesWithTagExpressions(boolQuery, tagSearchTarget.tagExpressions)
    return boolQuery
  }

  private fun addQueriesWithTagExpressions(boolQuery: BoolQueryBuilder, tagExpressions: List<String>) {
    for (tagExpression in tagExpressions) {
      val tagExpressionParts = getTagExpressionParts(tagExpression)
      if (isValidTagExpression(tagExpressionParts)) {
        val head = tagExpressionParts[0]
        val tail = tagExpressionParts[1]
        if (tail == ASTERISK) {
          continue
        }
        var tagKey = head
        var tagValue = tail

        when {
          isNegativeQuery(head) -> tagKey = head.substring(0, head.length - 1)
          isStartWithTilde(tail) -> tagValue = tail.substring(1, tail.length)
        }

        val tagField = "$TAGS_BUCKET_NAME.$tagKey"

        when {
          isNegativeQuery(head) -> {
            when {
              isStartWithTilde(tail) -> {
                when {
                  isRegexQuery(tagValue) -> boolQuery.mustNot(QueryBuilders.regexpQuery(tagField, getEscapedExpression(tagValue)))
                  else -> boolQuery.mustNot(QueryBuilders.prefixQuery(tagField, getEscapedExpression(tagValue)))
                }
              }
              else -> boolQuery.mustNot(QueryBuilders.termsQuery(tagField, terms(tagValue)))
            }
          }
          else -> {
            when {
              isStartWithTilde(tail) -> {
                when {
                  isRegexQuery(tagValue) -> boolQuery.filter(QueryBuilders.regexpQuery(tagField, getEscapedExpression(tagValue)))
                  else -> boolQuery.filter(QueryBuilders.prefixQuery(tagField, getEscapedExpression(tagValue)))
                }
              }
              else -> boolQuery.filter(QueryBuilders.termsQuery(tagField, terms(tagValue)))
            }
          }
        }
      }
    }
  }

  private fun addSearchTargetFilter(boolQuery: BoolQueryBuilder, tagSearchTarget: TagSearchTarget) {
    if (StringUtils.isNotBlank(tagSearchTarget.tagKey)) {
      val tagField = "$TAGS_BUCKET_NAME.${tagSearchTarget.tagKey}"
      boolQuery.filter(QueryBuilders.existsQuery(tagField))
      if (StringUtils.isNotBlank(tagSearchTarget.tagValue)) {
        boolQuery.filter(QueryBuilders.prefixQuery(tagField, tagSearchTarget.tagValue))
      }
    }
  }

  private fun terms(expr: String): List<String> {
    val result = mutableListOf<String>()
    var termBuilder = StringBuilder()

    for (c in expr) {
      if (skip(c)) {
        continue
      }

      if (c == ',' || c == '}') {
        result.add(termBuilder.toString())
        termBuilder = StringBuilder()
        continue
      }

      termBuilder.append(c)
    }

    if (StringUtils.isNotBlank(termBuilder.toString())) {
      result.add(termBuilder.toString())
    }

    return result
  }

  private fun isValidTagExpression(tagExpressionParts: List<String>) =
    tagExpressionParts.size == 2 && StringUtils.isNotBlank(tagExpressionParts[0]) && StringUtils.isNotBlank(tagExpressionParts[1])

  private fun getTagExpressionParts(tagExpression: String) = tagExpression.split(EQUALS)

  private fun isRegexQuery(expr: String) = StringUtils.containsAny(expr, *noPlainChars)

  private fun isNegativeQuery(expr: String) = expr.last() == EXCLAMATION

  private fun isStartWithTilde(expr: String) = expr.first() == TILDE

  private fun skip(char: Char) = char == '{' || char == ' '

  private fun getEscapedExpression(expr: String): String {
    return expr.replace(".", "\\.")
      .replace("*", "[^\\.]*")
      .replace("{", "(")
      .replace("{", "(")
      .replace("}", ")")
      .replace(",", "|")
      .replace("?", "[^\\.]")
  }

  companion object {
    const val TAGS_BUCKET_NAME = "tags"
    const val EQUALS = '='
    const val TILDE = '~'
    const val EXCLAMATION = '!'
    const val ASTERISK = "*"
    private val noPlainChars = charArrayOf('*', '?', '{', '(', '[')
  }
}
