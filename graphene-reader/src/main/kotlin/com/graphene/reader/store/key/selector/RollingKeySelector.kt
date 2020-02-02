package com.graphene.reader.store.key.selector

import com.graphene.common.key.RotationProperty
import com.graphene.common.key.TimeBasedRotationStrategy
import com.graphene.common.rule.GrapheneRules

class RollingKeySelector(
  private val keySelectorProperty: KeySelectorProperty
) : KeySelector {

  private val timeBasedRotationStrategy = TimeBasedRotationStrategy(RotationProperty(period = keySelectorProperty.period))

  override fun select(index: String, tenant: String, from: Long, to: Long): Set<String> {
    if (isDisableRotation()) {
      return setOf(index)
    }

    return timeBasedRotationStrategy.getRangeIndex(index, tenant, from, to)
  }

  private fun isDisableRotation() = keySelectorProperty.period == GrapheneRules.Key.ROTATION_NONE
}
