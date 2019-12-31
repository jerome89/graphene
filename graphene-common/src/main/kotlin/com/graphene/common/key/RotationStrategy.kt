package com.graphene.common.key

import com.graphene.common.rule.GrapheneRules
import java.lang.RuntimeException
import java.time.format.DateTimeFormatter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.threeten.extra.YearWeek

interface RotationStrategy {

  fun getIndexWithCurrentDate(index: String, tenant: String): String
  fun getIndexWithDate(index: String, tenant: String, timestampMillis: Long): String
  fun getRangeIndex(index: String, tenant: String, from: Long, to: Long): Set<String>

  companion object {
    fun of(rotationProperty: RotationProperty): RotationStrategy {
      return when (rotationProperty.strategy) {
        TIME_BASED_ROTATION -> TimeBasedRotationStrategy(rotationProperty)
        NO_OP_ROTATION -> NoOpRotationStrategy()
        else -> throw RotationNotSupportedException("${rotationProperty.strategy} is not supported!")
      }
    }

    private const val TIME_BASED_ROTATION = "timeBasedRotation"
    private const val NO_OP_ROTATION = "noOpRotation"
  }
}

class NoOpRotationStrategy : RotationStrategy {

  override fun getIndexWithCurrentDate(index: String, tenant: String): String = index

  override fun getIndexWithDate(index: String, tenant: String, timestampMillis: Long): String = index

  override fun getRangeIndex(index: String, tenant: String, from: Long, to: Long): Set<String> = setOf(index)
}

class TimeBasedRotationStrategy(
  rotationProperty: RotationProperty
) : RotationStrategy {
  override fun getIndexWithCurrentDate(index: String, tenant: String): String {
    val dateTime = DateTime()

    return when (timeUnit) {
      DAY -> GrapheneRules.index(index, tenant, timePattern.print(dateTime))
      else -> GrapheneRules.index(index, tenant, YearWeek.parse(timePattern.print(dateTime), DateTimeFormatter.ofPattern(DATE_FORMAT)).toString().toLowerCase())
    }
  }

  override fun getIndexWithDate(index: String, tenant: String, timestampMillis: Long): String {
    val dateTime = DateTime(timestampMillis)

    return when (timeUnit) {
      DAY -> GrapheneRules.index(index, tenant, timePattern.print(dateTime))
      else -> GrapheneRules.index(index, tenant, YearWeek.parse(timePattern.print(dateTime), DateTimeFormatter.ofPattern(DATE_FORMAT)).toString().toLowerCase())
    }
  }

  override fun getRangeIndex(index: String, tenant: String, from: Long, to: Long): Set<String> {
    val fromDateTime = DateTime(from)
    val toDateTime = DateTime(to)

    val indexes = mutableSetOf("${index}_${tenant}_${fromDateTime.weekyear}-w${withZero(fromDateTime.weekOfWeekyear)}", "${index}_${tenant}_${toDateTime.weekyear}-w${withZero(toDateTime.weekOfWeekyear)}")
    if (fromDateTime.weekyear == toDateTime.weekyear) {
      for (week in fromDateTime.weekOfWeekyear..toDateTime.weekOfWeekyear) {
        indexes.add("${index}_${tenant}_${fromDateTime.weekyear}-w${withZero(week)}")
      }
    } else {
      for (year in fromDateTime.weekyear..toDateTime.weekyear) {

        if (year > fromDateTime.weekyear && year < toDateTime.weekyear) {
          for (week in 1..52) {
            indexes.add("${index}_${tenant}_$year-w${withZero(week)}")
          }
        }

        if (year == fromDateTime.weekyear) {
          for (week in fromDateTime.weekOfWeekyear..52) {
            indexes.add("${index}_${tenant}_$year-w${withZero(week)}")
          }
        }

        if (year == toDateTime.weekyear) {
          for (week in 1..toDateTime.weekOfWeekyear) {
            indexes.add("${index}_${tenant}_$year-w${withZero(week)}")
          }
        }
      }
    }

    return indexes
  }

  private fun withZero(week: Int): String {
    return if (week < 10) {
      "0$week"
    } else {
      "$week"
    }
  }

  private var timeUnit = rotationProperty.period.toCharArray()[rotationProperty.period.lastIndex]
  private var timePattern = DateTimeFormat.forPattern(DATE_FORMAT)

  companion object {
    const val DATE_FORMAT = "yyyyMMdd"

    const val DAY = 'd'
  }
}

class RotationNotSupportedException(message: String) : RuntimeException(message)
