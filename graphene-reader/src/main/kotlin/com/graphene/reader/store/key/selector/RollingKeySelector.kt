package com.graphene.reader.store.key.selector

import com.graphene.common.key.RotationProperty
import com.graphene.common.key.TimeBasedRotationStrategy
import com.graphene.common.rule.GrapheneRules
import org.springframework.stereotype.Component

@Component
class RollingKeySelector(
  val keySelectorProperty: KeySelectorProperty
) : KeySelector {

  val timeBasedRotationStrategy = TimeBasedRotationStrategy(RotationProperty(period = keySelectorProperty.period))

  override fun select(index: String, tenant: String, from: Long, to: Long): Set<String> {
    if (isDisableRotation()) {
      return setOf(index)
    }

    return timeBasedRotationStrategy.getRangeIndex(index, tenant, from, to)
  }

  private fun isDisableRotation() = keySelectorProperty.period == GrapheneRules.Key.ROTATION_NONE
}
