package com.graphene.writer.store.key

interface KeyCache<K, V> {

  fun get(key: K): V?

  fun putIfAbsent(key: K, value: V): Boolean

  fun put(key: K, value: V): Boolean

}
