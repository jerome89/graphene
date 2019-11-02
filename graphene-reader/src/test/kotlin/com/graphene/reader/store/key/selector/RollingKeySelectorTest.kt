package com.graphene.reader.store.key.selector

import com.graphene.common.rule.GrapheneRules
import com.graphene.common.utils.DateTimeUtils
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RollingKeySelectorTest {

  @Test
  internal fun `should return single index if rotation is disable`() {
    // given
    val rollingKeySelector = RollingKeySelector(KeySelectorProperty(GrapheneRules.Key.DEFAULT_TYPE, GrapheneRules.Key.ROTATION_NONE))

    // when
    val indexes = rollingKeySelector.select("index", "tenant", DateTimeUtils.from("2019-12-01 10:00:00"), DateTimeUtils.from("2019-12-30 10:00:00"))

    // then
    assertEquals(setOf("index"), indexes)
  }
}
