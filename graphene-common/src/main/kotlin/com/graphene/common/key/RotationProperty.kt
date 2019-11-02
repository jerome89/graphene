package com.graphene.common.key

import com.graphene.common.rule.GrapheneRules
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "graphene.writer.store.key.rotation")
data class RotationProperty(
  var strategy: String = GrapheneRules.DEFAULT_ROTATION_STRATEGY,
  var period: String = GrapheneRules.Key.ROTATION_NONE
)
