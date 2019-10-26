package com.graphene.writer.store.key.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "graphene.writer.store.key.rotation")
class RotationProperty {
  var strategy: String = DEFAULT_STRATEGY
  var period: String = PERIOD

  companion object {
    const val DEFAULT_STRATEGY = "timeBasedRotation"
    const val PERIOD = "1d"
  }
}
