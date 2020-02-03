package com.graphene.reader.store.key.elasticsearch.selector

import com.graphene.common.rule.GrapheneRules

class KeySelectorProperty(
  var type: String? = null,
  var period: String = GrapheneRules.Key.ROTATION_NONE
) {

  companion object {
    fun default(): KeySelectorProperty {
      return KeySelectorProperty(
        GrapheneRules.Key.DEFAULT_TYPE,
        GrapheneRules.Key.ROTATION_NONE
      )
    }
  }
}
