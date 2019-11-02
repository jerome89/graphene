package com.graphene.writer.store.key.property

import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import java.text.SimpleDateFormat
import kotlin.test.assertEquals
import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.Test

internal class TimeBasedRotationStrategyTest {

  @Test
  internal fun `should return current time with day unit`() {
    table(
      headers("currentTime", "expectedIndexDate"),
      row("2019-10-01 00:00:00", "20191001"),
      row("2019-10-02 00:00:00", "20191002")
    ).forAll { currentTime, expectedIndexDate ->
      // given
      val rotationProperty = RotationProperty(period = "1d")
      setCurrentMillisFixed(currentTime)

      // when
      val timeBasedRotationStrategy = TimeBasedRotationStrategy(rotationProperty)

      // then
      assertEquals(expectedIndexDate, timeBasedRotationStrategy.getDate())
    }
  }

  @Test
  internal fun `should return current time with week unit`() {
    table(
      headers("currentTime", "expectedIndexDate"),
      row("2019-10-01 00:00:00", "2019-w40"),
      row("2019-10-02 00:00:00", "2019-w40"),
      row("2019-10-03 00:00:00", "2019-w40"),
      row("2019-10-04 00:00:00", "2019-w40"),
      row("2019-10-05 00:00:00", "2019-w40"),
      row("2019-10-06 00:00:00", "2019-w40"),
      row("2019-10-07 00:00:00", "2019-w41"),
      row("2020-01-01 00:00:00", "2020-w01"),
      row("2020-01-08 00:00:00", "2020-w02")
    ).forAll { currentTime, expectedIndexDate ->
      // given
      val rotationProperty = RotationProperty(period = "1w")
      setCurrentMillisFixed(currentTime)

      // when
      val timeBasedRotationStrategy = TimeBasedRotationStrategy(rotationProperty)

      // then
      assertEquals(expectedIndexDate, timeBasedRotationStrategy.getDate())
    }
  }

  private fun setCurrentMillisFixed(date: String) {
    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).time
    DateTimeUtils.setCurrentMillisFixed(currentTime)
  }
}
