package com.graphene.common.key

import com.graphene.common.rule.GrapheneRule
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "graphene.writer.store.key.rotation")
data class RotationProperty(
  var strategy: String = GrapheneRule.DEFAULT_ROTATION_STRATEGY,
  var period: String = GrapheneRule.DEFAULT_PERIOD
)
