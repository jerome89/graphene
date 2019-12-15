package com.graphene.writer.input.influxdb

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

internal class InfluxDbMetricConverterTest {

  @Test
  internal fun `should convert to graphene metric format#1`() {
    // given
    val influxDbMetricConverter = InfluxDbMetricConverter()

    // when
    val grapheneMetric = influxDbMetricConverter.convert("cpu,cpu=cpu1,host=server1 usage_user=8.4 1576318510000000000")

    // then influx db format to graphene metric

    // measurement
    assertEquals("cpu", grapheneMetric.meta["@measurement"])

    // tag
    assertEquals(mapOf(Pair("cpu", "cpu1"), Pair("host", "server1")), grapheneMetric.tags)

    // field
    assertEquals(8.4, grapheneMetric.metrics["usage_user"])

    // timestamp
    assertEquals(1576318510, grapheneMetric.timestampSeconds)
  }

  @Test
  internal fun `should convert to graphene metric format#2`() {
    // given
    val influxDbMetricConverter = InfluxDbMetricConverter()

    // when
    val grapheneMetric = influxDbMetricConverter.convert("disk,device=disk1s4,fstype=apfs,host=server1,mode=rw,path=/private/var/vm used=3221245952i,used_percent=2.102430211035405,inodes_total=9223372036854775807i,inodes_free=9223372036854775804i,inodes_used=3i,total=250685575168i,free=149994110976i 1576383340000000000")

    // then influx db format to graphene metric

    // measurement
    assertEquals("disk", grapheneMetric.meta["@measurement"])

    // tag
    assertEquals(mapOf(
      Pair("device", "disk1s4"),
      Pair("fstype", "apfs"),
      Pair("host", "server1"),
      Pair("mode", "rw"),
      Pair("path", "/private/var/vm")
    ), grapheneMetric.tags)

    assertEquals(3221245952.0, grapheneMetric.metrics["used"])
    assertEquals(2.102430211035405, grapheneMetric.metrics["used_percent"])
    assertEquals(9223372036854775807.0, grapheneMetric.metrics["inodes_total"])
    assertEquals(9223372036854775804.0, grapheneMetric.metrics["inodes_free"])
    assertEquals(3.0, grapheneMetric.metrics["inodes_used"])
    assertEquals(250685575168.0, grapheneMetric.metrics["total"])
    assertEquals(149994110976.0, grapheneMetric.metrics["free"])

    // timestamp
    assertEquals(1576383340, grapheneMetric.timestampSeconds)
  }
}
