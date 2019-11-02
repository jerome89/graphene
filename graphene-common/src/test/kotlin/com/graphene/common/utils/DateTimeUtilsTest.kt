package com.graphene.common.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DateTimeUtilsTest {

  @Test
  internal fun `should return epoch time milliseconds`() {
    assertEquals(DateTimeUtils.from("2019-10-10 10:00:00"), 1570669200000)
  }
}
