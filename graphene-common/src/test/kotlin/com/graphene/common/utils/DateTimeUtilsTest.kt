package com.graphene.common.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DateTimeUtilsTest {

  @Test
  internal fun `should return epoch time milliseconds`() {
    assertEquals(1570701600000, DateTimeUtils.from("2019-10-10 10:00:00"))
  }
}
