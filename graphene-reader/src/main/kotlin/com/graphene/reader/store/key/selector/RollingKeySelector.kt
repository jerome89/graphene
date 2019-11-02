package com.graphene.reader.store.key.selector

import com.graphene.common.key.RotationProperty
import com.graphene.common.key.TimeBasedRotationStrategy
import com.graphene.common.rule.GrapheneRule
import org.joda.time.Instant
import org.springframework.stereotype.Component

@Component
class RollingKeySelector(
  val keySelectorProperty: KeySelectorProperty
) : KeySelector {

  val timeBasedRotationStrategy = TimeBasedRotationStrategy(RotationProperty(period = keySelectorProperty.period))

  override fun select(index: String, tenant: String, from: Long, to: Long): Set<String> {
    if (keySelectorProperty.period == GrapheneRule.DEFAULT_PERIOD) {
      return setOf(index)
    }

    val fromDate = timeBasedRotationStrategy.getDate(Instant().withMillis(from))
    val toDate = timeBasedRotationStrategy.getDate(Instant().withMillis(to))

    return setOf("${index}_${tenant}_${fromDate}", "${index}_${tenant}_${toDate}")
  }
}
