package com.graphene.reader.store.key.selector

import com.graphene.common.rule.GrapheneRules
import com.graphene.common.utils.DateTimeUtils
import io.kotlintest.matchers.collections.shouldContainAll
import org.junit.jupiter.api.Test

internal class RollingKeySelectorTest {

  @Test
  internal fun `should return single index if rotation is disable`() {
    // given
    val rollingKeySelector = RollingKeySelector(KeySelectorProperty(GrapheneRules.Key.DEFAULT_TYPE, GrapheneRules.Key.ROTATION_NONE))

    // when
    val indexes = rollingKeySelector.select("index", "tenant", DateTimeUtils.from("2019-12-01 10:00:00"), DateTimeUtils.from("2019-12-30 10:00:00"))

    // then
    setOf("index") shouldContainAll indexes
  }

  @Test
  internal fun `should return time based index if rotation is enable`() {
    // given
    val rollingKeySelector = RollingKeySelector(KeySelectorProperty(GrapheneRules.Key.DEFAULT_TYPE, GrapheneRules.Key.ROTATION_1W))

    // when
    val indexes = rollingKeySelector.select("index", "tenant", DateTimeUtils.from("2019-12-01 10:00:00"), DateTimeUtils.from("2019-12-30 10:00:00"))

    // then
    setOf("index_tenant_2019-w48", "index_tenant_2019-w49", "index_tenant_2019-w50", "index_tenant_2019-w51", "index_tenant_2019-w52", "index_tenant_2020-w01") shouldContainAll indexes
  }
}
