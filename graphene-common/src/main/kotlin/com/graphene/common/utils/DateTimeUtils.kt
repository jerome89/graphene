package com.graphene.common.utils

import com.graphene.common.rule.GrapheneRules
import java.text.SimpleDateFormat
import java.util.regex.Pattern
import org.joda.time.DateTimeUtils
import java.util.*

/**
 * @author Andrei Ivanov
 * @author dark
 */
object DateTimeUtils {

  private val timeOffsetPattern = Pattern.compile("^([+-]?)(\\d+)([a-z]+)$")

  /**
   * Parses time offset from string (Examples: "-1d", "+1mon")
   *
   * @param s string to parse
   * @return number of seconds in the offset
   */
  fun parseTimeOffset(s: String): Long {
    val matcher = timeOffsetPattern.matcher(s.replace("^['\"]|['\"]$".toRegex(), ""))

    if (!matcher.matches()) return 0L

    val sign = if (matcher.group(1) == "+") 1 else -1
    val offset = Integer.parseInt(matcher.group(2)) * getUnitValue(matcher.group(3))

    return offset * sign
  }

  private fun getUnitValue(s: String): Long {
    return if (s.startsWith("s")) {
      1
    } else if (s.startsWith("min")) {
      60
    } else if (s.startsWith("h")) {
      3600
    } else if (s.startsWith("d")) {
      86400
    } else if (s.startsWith("w")) {
      604800
    } else if (s.startsWith("mon")) {
      18144000
    } else if (s.startsWith("y")) {
      31536000L
    } else {
      60L
    }
  }

  fun testTimeOffset(s: String): Boolean {
    return timeOffsetPattern.matcher(s.replace("^['\"]|['\"]$".toRegex(), "")).matches()
  }

  fun currentTimeSeconds(): Long {
    return DateTimeUtils.currentTimeMillis() / 1000L
  }

  fun currentTimeMillis(): Long {
    return DateTimeUtils.currentTimeMillis()
  }

  fun from(formattedDate: String): Long {
    val simpleDateFormat = SimpleDateFormat(GrapheneRules.Date.DATE_FORMAT)
    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return simpleDateFormat.parse(formattedDate).time
  }
}
