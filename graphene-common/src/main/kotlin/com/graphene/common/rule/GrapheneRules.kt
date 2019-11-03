package com.graphene.common.rule

object GrapheneRules {
  const val DEFAULT_ROTATION_STRATEGY = "timeBasedRotation"
  const val DEFAULT_TENANT = "NONE"

  object Date {
    const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
  }

  object Key {
    const val DEFAULT_TYPE = "path"
    const val ROTATION_NONE = "0"

    fun extractWeekFrom(weeklyDate: String): Int {
      return weeklyDate.substring(6, weeklyDate.length).toInt()
    }
  }
}
