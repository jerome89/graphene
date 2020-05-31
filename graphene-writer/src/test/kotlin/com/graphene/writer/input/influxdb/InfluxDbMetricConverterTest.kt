package com.graphene.writer.input.influxdb

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

internal class InfluxDbMetricConverterTest {

  @Test
  internal fun `should convert to graphene metric format#1`() {
    // given
    val influxDbMetricConverter = InfluxDbMetricConverter()

    // when
    val grapheneMetric = influxDbMetricConverter.convert("cpu,cpu=cpu1,host=server1 usage_user=8.4 1576318510000000000")[0]

    // then influx db format to graphene metric

    // measurement
    assertEquals("cpu", grapheneMetric.meta["@measurement"])

    // tag
    assertEquals(mapOf(Pair("cpu", "cpu1"), Pair("host", "server1")), grapheneMetric.tags)

    // field
    assertEquals(8.4, grapheneMetric.value)

    // timestamp
    assertEquals(1576318510, grapheneMetric.timestampSeconds)
  }

  @Test
  internal fun `should convert to graphene metric format#2`() {
    // given
    val influxDbMetricConverter = InfluxDbMetricConverter()

    // when
    val grapheneMetrics = influxDbMetricConverter.convert("disk,device=disk1s4,fstype=apfs,host=server1,mode=rw,path=/private/var/vm used=3221245952i,used_percent=2.102430211035405,free=149994110976i 1576383340000000000")

    // then influx db format to graphene metric

    // then #1
    var grapheneMetric = grapheneMetrics[0]

    assertEquals("disk", grapheneMetric.meta["@measurement"])

    // id
    assertEquals("disk.used;device=disk1s4&fstype=apfs&host=server1&mode=rw&path=/private/var/vm", grapheneMetric.id)
    assertEquals("disk.used", grapheneMetric.metricKey())

    // tag
    assertEquals(mapOf(
      Pair("device", "disk1s4"),
      Pair("fstype", "apfs"),
      Pair("host", "server1"),
      Pair("mode", "rw"),
      Pair("path", "/private/var/vm")
    ), grapheneMetric.tags)

    assertEquals(3221245952.0, grapheneMetric.value)

    // timestamp
    assertEquals(1576383340, grapheneMetric.timestampSeconds)

    // then #2
    grapheneMetric = grapheneMetrics[1]

    assertEquals("disk", grapheneMetric.meta["@measurement"])

    // id
    assertEquals("disk.used_percent;device=disk1s4&fstype=apfs&host=server1&mode=rw&path=/private/var/vm", grapheneMetric.id)
    assertEquals("disk.used_percent", grapheneMetric.metricKey())

    // tag
    assertEquals(mapOf(
      Pair("device", "disk1s4"),
      Pair("fstype", "apfs"),
      Pair("host", "server1"),
      Pair("mode", "rw"),
      Pair("path", "/private/var/vm")
    ), grapheneMetric.tags)

    assertEquals(2.102430211035405, grapheneMetric.value)

    // then #3
    grapheneMetric = grapheneMetrics[2]

    // id
    assertEquals("disk.free;device=disk1s4&fstype=apfs&host=server1&mode=rw&path=/private/var/vm", grapheneMetric.id)
    assertEquals("disk.free", grapheneMetric.metricKey())
    assertEquals(149994110976.0, grapheneMetric.value)

    // timestamp
    assertEquals(1576383340, grapheneMetric.timestampSeconds)
  }

  @Test
  internal fun `should convert to graphene metric format#3`() {
    // given
    val influxDbMetricConverter = InfluxDbMetricConverter()

    // when
    val grapheneMetric = influxDbMetricConverter.convert("cpu,cpu=cpu1,host=server1 usage_user=8.4 1576318510000000000\n")[0]

    // then influx db format to graphene metric

    // measurement
    assertEquals("cpu", grapheneMetric.meta["@measurement"])

    // tag
    assertEquals(mapOf(Pair("cpu", "cpu1"), Pair("host", "server1")), grapheneMetric.tags)

    // field
    assertEquals(8.4, grapheneMetric.value)

    // timestamp
    assertEquals(1576318510, grapheneMetric.timestampSeconds)
  }
}
