package com.graphene.reader.store.key.selector

import com.graphene.common.rule.GrapheneRules
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("graphene.reader.store.key.handlers.elasticsearch-key-search-handler.key-selector")
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
