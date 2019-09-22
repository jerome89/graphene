package net.iponweb.disthene.reader.utils

import com.google.common.base.Strings

object MetricRule {

  private const val UNKNOWN = "unknown"
  private const val NONE = "NONE"

  fun generate(key: String): String {
    if (Strings.isNullOrEmpty(key)) {
      return UNKNOWN
    }

    return key
  }

  fun unknownKey(): String {
    return UNKNOWN
  }

  @JvmStatic
  fun defaultTenant(): String {
    return NONE
  }
}
