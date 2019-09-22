package com.graphene.writer.input.graphite

import com.graphene.writer.input.GrapheneMetric
import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class GraphiteCodecTest {

  private val graphiteCodec = GraphiteCodec()

  @Test
  internal fun `should encode to graphene metric`() {
    // given
    val key = "a.b.c"
    val value = 1.0
    val timestamp = DateTimeUtils.currentTimeMillis()

    // when
    val grapheneMetric = graphiteCodec.encode(GraphiteMetric(
      key,
      value,
      timestamp
    ))

    // then
    assertEquals(GrapheneMetric(
      Collections.emptyMap(),
      mapOf(Pair("0", "a"), Pair("1", "b"), Pair("2", "c")),
      value,
      timestamp
    ), grapheneMetric)
  }

  @Test
  internal fun `should encode to graphene metric with unknown key`() {
    // given
    val key = "a.b..c"
    val value = 1.0
    val timestamp = DateTimeUtils.currentTimeMillis()

    // when
    val grapheneMetric = graphiteCodec.encode(GraphiteMetric(
      key,
      value,
      timestamp
    ))

    // then
    assertEquals(GrapheneMetric(
      Collections.emptyMap(),
      mapOf(Pair("0", "a"), Pair("1", "b"), Pair("2", "unknown"), Pair("3", "c")),
      value,
      timestamp
    ), grapheneMetric)
  }
}
