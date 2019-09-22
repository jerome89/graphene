package net.iponweb.disthene.reader.utils

import com.google.common.base.Strings

object MetricRule {

  private const val DEFAULT_UNKNOWN_KEY = "unknown"

  fun generate(key: String): String {
    if (Strings.isNullOrEmpty(key)) {
      return DEFAULT_UNKNOWN_KEY
    }

    return key
  }

  fun unknownKey(): String {
    return DEFAULT_UNKNOWN_KEY
  }

}
