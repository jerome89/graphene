package com.graphene.writer.store.key

import com.google.common.cache.CacheBuilder
import java.util.Objects
import java.util.concurrent.TimeUnit

/**
 *
 * This class is not thread safe.
 * If you need to handle parallel operation, please consider double-check locking at getOrCreate method.
 *
 * @author dark
 */
class TimeBasedLocalKeyCache(
  expireInterval: Long
) : KeyCache {

  private val internalTimeBasedCache = CacheBuilder.newBuilder()
    .expireAfterAccess(expireInterval, TimeUnit.MINUTES)
    .build<String, Byte?>()

  override fun get(key: String): Byte? {
    return internalTimeBasedCache.getIfPresent(key)
  }

  override fun putIfAbsent(key: String): Boolean {
    return if (Objects.nonNull(get(key))) false
    else put(key)
  }

  override fun put(key: String): Boolean {
    internalTimeBasedCache.put(key, 0.toByte())
    return true
  }
}
