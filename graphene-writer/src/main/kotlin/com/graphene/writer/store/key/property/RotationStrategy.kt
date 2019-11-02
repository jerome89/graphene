package com.graphene.writer.store.key.property

import java.time.format.DateTimeFormatter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.threeten.extra.YearWeek

interface RotationStrategy {

  fun getDate(): String

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

  private var timeUnit = rotationProperty.period.toCharArray()[rotationProperty.period.lastIndex]
  private var timePattern = DateTimeFormat.forPattern(DATE_FORMAT)

  override fun getDate(): String {
    val dateTime = DateTime()

    return when (timeUnit) {
      DAY -> timePattern.print(dateTime)
      else -> YearWeek.parse(timePattern.print(dateTime), DateTimeFormatter.ofPattern(DATE_FORMAT)).toString().toLowerCase()
    }
  }

  companion object {
    const val DATE_FORMAT = "yyyyMMdd"
    const val DAY = 'd'
  }
}
