package com.graphene.common.utils

import org.apache.commons.lang3.StringUtils

/**
 * @author Andrei Ivanov
 */
object WildcardUtils {

  fun isPlainPath(path: String): Boolean {
    val noPlainChars = charArrayOf('*', '?', '{', '(', '[')
    return !StringUtils.containsAny(path, *noPlainChars)
  }

  fun getPathsRegExFromWildcard(wildcard: String): String {
    return wildcard.replace(".", "\\.")
      .replace("*", "[^\\.]*")
      .replace("{", "(")
      .replace("}", ")")
      .replace(",", "|")
      .replace("?", "[^\\.]")
  }

}
