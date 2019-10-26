package com.graphene.writer.store.key.property

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface RotationStrategy {

  fun getDate(): String

  companion object {
    fun of(rotationProperty: RotationProperty): RotationStrategy {
      return when (rotationProperty.strategy) {
        "timeBasedRotation" -> TimeBasedRotationStrategy(rotationProperty)
        else -> TimeBasedRotationStrategy(rotationProperty)
      }
    }
  }
}

class TimeBasedRotationStrategy(
  val rotationProperty: RotationProperty
) : RotationStrategy {

  override fun getDate(): String {
    val currentTime = LocalDateTime.now()
    return currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
  }

}
