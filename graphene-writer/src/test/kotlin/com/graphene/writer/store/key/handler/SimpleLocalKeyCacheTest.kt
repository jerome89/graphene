package com.graphene.writer.store.key.handler

import com.graphene.writer.store.key.SimpleLocalKeyCache
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import org.junit.jupiter.api.Test

internal class SimpleLocalKeyCacheTest {

  @Test
  internal fun `should return null if there's no key in cache`() {
    // given
    val simpleCache = SimpleLocalKeyCache<String>(1)

    // when
    // nothing

    // then
    assertNull(simpleCache.get(KEY))
  }

  @Test
  internal fun `should return false if key is already in cache`() {
    // given
    val timeBasedCache = SimpleLocalKeyCache<String>(1)

    // when
    timeBasedCache.put(KEY)

    // then
    assertFalse(timeBasedCache.putIfAbsent(KEY))
  }

  @Test
  internal fun `should return key if key is in cache`() {
    // given
    val timeBasedCache = SimpleLocalKeyCache<String>(1)

    // when
    timeBasedCache.put(KEY)

    // then
    assertEquals(timeBasedCache.get(KEY), KEY)
  }

  companion object {
    const val KEY = "key1"
  }
}
