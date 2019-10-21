package com.graphene.reader.store.key.selector

import org.springframework.stereotype.Component

@Component
class ElasticsearchKeySelector : KeySelector {

  override fun select(index: String, tenant: String, from: Long, to: Long): String {
    return "${index}.${tenant}.CURRENT"
  }

}
