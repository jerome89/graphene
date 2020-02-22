package com.graphene.writer.store.key

interface KeyCache {

  fun get(key: String): Byte?

  fun putIfAbsent(key: String): Boolean

  fun put(key: String): Boolean
}
