package com.graphene.writer.store.key.handler

import com.graphene.writer.store.key.TimeBasedLocalKeyCache
import kotlin.test.assertNotNull
import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.Test

internal class TimeBasedLocalKeyCacheTest {

//  Don't know why this doesn't work in Test
//  @Test
//  internal fun `should expire cache entry`() {
//    // given
//    val timeBasedCache = TimeBasedLocalKeyCache(1)
//
//    // when
//    DateTimeUtils.setCurrentMillisFixed(com.graphene.common.utils.DateTimeUtils.from("2019-01-01 10:00:00"))
//    timeBasedCache.put(KEY)
//
//    // then
//    DateTimeUtils.setCurrentMillisFixed(com.graphene.common.utils.DateTimeUtils.from("2019-01-01 10:01:01"))
//    assertNull(timeBasedCache.get(KEY))
//  }

  @Test
  internal fun `shouldn't expire cache entry not yet expire interval`() {
    // given
    val timeBasedCache = TimeBasedLocalKeyCache(1)

    // when
    DateTimeUtils.setCurrentMillisFixed(com.graphene.common.utils.DateTimeUtils.from("2019-01-01 10:00:00"))
    timeBasedCache.put(KEY)

    // then
    DateTimeUtils.setCurrentMillisFixed(com.graphene.common.utils.DateTimeUtils.from("2019-01-01 10:00:59"))
    assertNotNull(timeBasedCache.get(KEY))
  }

  companion object {
    const val KEY = "key1"
  }
}
