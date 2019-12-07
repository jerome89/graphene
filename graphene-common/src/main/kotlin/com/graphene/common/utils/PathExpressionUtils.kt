package com.graphene.common.utils

import org.apache.commons.lang3.StringUtils

/**
 * @author Andrei Ivanov
 */
object PathExpressionUtils {

  fun isPlainPath(pathExpression: String): Boolean {
    val noPlainChars = charArrayOf('*', '?', '{', '(', '[')
    return !StringUtils.containsAny(pathExpression, *noPlainChars)
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
