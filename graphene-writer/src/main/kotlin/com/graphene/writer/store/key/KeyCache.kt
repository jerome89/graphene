package com.graphene.writer.store.key

interface KeyCache<K> {

  fun get(key: K): K?

  fun putIfAbsent(key: K): Boolean

  fun put(key: K): Boolean
}
