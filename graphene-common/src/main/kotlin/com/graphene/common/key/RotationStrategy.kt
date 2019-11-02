package com.graphene.common.key

import java.time.format.DateTimeFormatter
import org.joda.time.DateTime
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat
import org.threeten.extra.YearWeek

interface RotationStrategy {

  fun getDate(): String
  fun getRangeIndex(index: String, tenant: String, from: Long, to: Long): Set<String>

  companion object {
    fun of(rotationProperty: RotationProperty): RotationStrategy {
      return when (rotationProperty.strategy) {
        TIME_BASED_ROTATION -> TimeBasedRotationStrategy(rotationProperty)
        else -> TimeBasedRotationStrategy(rotationProperty)
      }
    }

    private const val TIME_BASED_ROTATION = "timeBasedRotation"
  }
}

class TimeBasedRotationStrategy(
  rotationProperty: RotationProperty
) : RotationStrategy {

  override fun getRangeIndex(index: String, tenant: String, from: Long, to: Long): Set<String> {
    val fromDateTime = DateTime(from)
    val toDateTime = DateTime(to)

    val indexes = mutableSetOf("${index}_${tenant}_${fromDateTime.weekyear}-w${fromDateTime.weekOfWeekyear}", "${index}_${tenant}_${toDateTime.weekyear}-w${toDateTime.weekOfWeekyear}")
    if (fromDateTime.weekyear == toDateTime.weekyear) {
      for (week in fromDateTime.weekOfWeekyear .. toDateTime.weekOfWeekyear) {
        indexes.add("${index}_${tenant}_${fromDateTime.weekyear}-w${week}")
      }
    } else {
      for (year in fromDateTime.weekyear .. toDateTime.weekyear) {

        if (year > fromDateTime.weekyear && year < toDateTime.weekyear) {
          for (week in 1 .. 52) {
            indexes.add("${index}_${tenant}_${year}-w${week}")
          }
        }

        if (year == fromDateTime.weekyear) {
          for (week in fromDateTime.weekOfWeekyear .. 52) {
            indexes.add("${index}_${tenant}_${year}-w${week}")
          }
        }

        if (year == toDateTime.weekyear) {
          for (week in 1..toDateTime.weekOfWeekyear) {
            indexes.add("${index}_${tenant}_${year}-w${week}")
          }
        }
      }
    }

    return indexes
  }

  private var timeUnit = rotationProperty.period.toCharArray()[rotationProperty.period.lastIndex]
  private var timePattern = DateTimeFormat.forPattern(DATE_FORMAT)

  override fun getDate(): String {
    val dateTime = DateTime()

    return when (timeUnit) {
      DAY -> timePattern.print(dateTime)
      else -> YearWeek.parse(timePattern.print(dateTime), DateTimeFormatter.ofPattern(DATE_FORMAT)).toString().toLowerCase()
    }
  }

  fun getDate(timeMillis: Long): String {
    val dateTime = DateTime(timeMillis)

    return when (timeUnit) {
      DAY -> timePattern.print(timeMillis)
      else -> "${dateTime.weekyear}-w${dateTime.weekOfWeekyear}"
    }
  }

  companion object {
    const val DATE_FORMAT = "yyyyMMdd"

    const val DAY = 'd'
  }
}
