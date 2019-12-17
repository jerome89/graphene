package com.graphene.common.utils

import org.apache.commons.lang3.StringUtils

/**
 * @author Andrei Ivanov
 * @author dark
 */
object PathExpressionUtils {

  fun isPlainPath(pathExpression: String, noPlainChars: CharArray = charArrayOf('*', '?', '{', '(', '[')): Boolean {
    return !StringUtils.containsAny(pathExpression, *noPlainChars)
  }

  fun hasOnlyOrCondition(pathExpression: String): Boolean {
    return isPlainPath(pathExpression, charArrayOf('*', '?', '(', '[')) &&
      !isPlainPath(pathExpression, charArrayOf('{'))
  }

  fun getEscapedPathExpression(pathExpression: String): String {
    return pathExpression.replace(".", "\\.")
      .replace("*", "[^\\.]*")
      .replace("{", "(")
      .replace("}", ")")
      .replace(",", "|")
      .replace("?", "[^\\.]")
  }
}
