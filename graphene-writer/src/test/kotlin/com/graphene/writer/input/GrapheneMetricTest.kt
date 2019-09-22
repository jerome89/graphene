package com.graphene.writer.input

import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class GrapheneMetricTest {

  @Test
  internal fun `should return graphite key joined with dot ordered by numeric`() {
    // given
    val grapheneMetric = GrapheneMetric(
      Collections.emptyMap(),
      mapOf(
        Pair("0", "a"),
        Pair("1", "b"),
        Pair("2", "c")
      ),
      1.0,
      DateTimeUtils.currentTimeMillis()
    )

    // when
    val graphiteKey = grapheneMetric.getGraphiteKey()

    // then
    assertEquals(graphiteKey, "a.b.c")
  }
}
