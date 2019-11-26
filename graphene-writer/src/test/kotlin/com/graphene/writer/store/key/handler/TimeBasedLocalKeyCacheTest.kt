package com.graphene.writer.store.key.handler

import com.graphene.writer.store.key.TimeBasedLocalKeyCache
import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class TimeBasedLocalKeyCacheTest {

  @Test
  internal fun `should expire cache entry`() {
    // given
    val timeBasedCache = TimeBasedLocalKeyCache<String, Long>(1)

    // when
    DateTimeUtils.setCurrentMillisFixed(com.graphene.common.utils.DateTimeUtils.from("2019-01-01 10:00:00"))
    timeBasedCache.put(KEY, VALUE)

    // then
    DateTimeUtils.setCurrentMillisFixed(com.graphene.common.utils.DateTimeUtils.from("2019-01-01 10:01:01"))
    assertNull(timeBasedCache.get(KEY))
  }

  @Test
  internal fun `shouldn't expire cache entry not yet expire interval`() {
    // given
    val timeBasedCache = TimeBasedLocalKeyCache<String, Long>(1)

    // when
    DateTimeUtils.setCurrentMillisFixed(com.graphene.common.utils.DateTimeUtils.from("2019-01-01 10:00:00"))
    timeBasedCache.put(KEY, VALUE)

    // then
    DateTimeUtils.setCurrentMillisFixed(com.graphene.common.utils.DateTimeUtils.from("2019-01-01 10:00:59"))
    assertEquals(timeBasedCache.get(KEY), VALUE)
  }

  companion object {
    const val KEY = "key1"
    const val VALUE = 1L
  }
}
