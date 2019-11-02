package com.graphene.reader.store.key.selector

interface KeySelector {

  fun select(index: String, tenant: String, from: Long, to: Long): Set<String>
}
