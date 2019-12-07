package com.graphene.writer.store.key

import com.google.common.cache.CacheBuilder
import com.graphene.common.utils.DateTimeUtils
import java.util.Objects
import java.util.concurrent.TimeUnit

/**
 *
 * This class is not thread safe.
 * If you need to handle parallel operation, please consider double-check locking at getOrCreate method.
 *
 * @author dark
 */
class TimeBasedLocalKeyCache<K, V>(
  private val expireInterval: Long
) : KeyCache<K, V> {

  private val internalTimeBasedCache = CacheBuilder.newBuilder()
    .expireAfterAccess(expireInterval, TimeUnit.MINUTES)
    .build<Long, MutableMap<K, V>>()

  override fun get(key: K): V? {
    return getOrCreate()[key]
  }

  override fun putIfAbsent(key: K, value: V): Boolean {
    return if (getOrCreate().contains(key)) false
    else put(key, value)
  }

  override fun put(key: K, value: V): Boolean {
    getOrCreate()[key] = value
    return true
  }

  private fun getOrCreate(): MutableMap<K, V> {
    val normalizedTimeBucket = DateTimeUtils.currentTimeMillis() / TimeUnit.MINUTES.toMillis(expireInterval) * TimeUnit.MINUTES.toMillis(expireInterval)
    var cache = internalTimeBasedCache.getIfPresent(normalizedTimeBucket)

    if (Objects.isNull(cache)) {
      cache = mutableMapOf()
      internalTimeBasedCache.put(normalizedTimeBucket, cache)
    }

    return internalTimeBasedCache.getIfPresent(normalizedTimeBucket)!!
  }
}
