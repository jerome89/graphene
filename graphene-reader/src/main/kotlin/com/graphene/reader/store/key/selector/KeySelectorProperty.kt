package com.graphene.reader.store.key.selector

import com.graphene.common.rule.GrapheneRule
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("graphene.reader.store.key.handlers.elasticsearch-key-search-handler.key-selector")
class KeySelectorProperty {
  var type: String? = null
  var period: String = GrapheneRule.DEFAULT_PERIOD
}
