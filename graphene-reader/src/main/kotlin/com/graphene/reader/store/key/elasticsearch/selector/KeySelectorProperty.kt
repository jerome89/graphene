package com.graphene.reader.store.key.elasticsearch.selector

import com.graphene.common.rule.GrapheneRules
import org.springframework.boot.context.properties.ConfigurationProperties

// TODO Please fix me to support both of index and simple key property
@ConfigurationProperties("graphene.reader.store.key.handlers.index-based-key-search-handler.key-selector")
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
