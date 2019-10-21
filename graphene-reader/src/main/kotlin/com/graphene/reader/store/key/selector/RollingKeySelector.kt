package com.graphene.reader.store.key.selector

import org.springframework.stereotype.Component

@Component
class RollingKeySelector(
  val keySelectorProperty: KeySelectorProperty
) : KeySelector {

  override fun select(index: String, tenant: String, from: Long, to: Long): String {
    if (keySelectorProperty.period == "0") {
      return "${index}.${tenant}.CURRENT"
    }

    return "${index}.${tenant}.CURRENT"
  }

}
