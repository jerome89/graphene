package com.graphene.writer.input.kafka.deserializer

import com.graphene.writer.input.Source
import io.kotlintest.matchers.maps.shouldContainAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.Test

internal class PrometheusDeserializerTest {

  private val prometheusDeserializer = PrometheusDeserializer()

  @Test
  internal fun `should ignore if plain prometheus metric is invalid`() {
    val grapheneMetric = prometheusDeserializer.deserialize("prometheus.topic", INVALID_PROMETHEUS_METRIC.toByteArray())

    grapheneMetric.size shouldBe 0
  }

  @Test
  internal fun `should deserialize from single prometheus metric`() {
    val grapheneMetric = prometheusDeserializer.deserialize("prometheus.topic", SINGLE_PROMETHEUS_METRIC.toByteArray())

    grapheneMetric shouldNotBe null

    grapheneMetric[0].id shouldBe "http_requests_total;method=post;code=200"
    grapheneMetric[0].tags shouldContainAll mapOf(Pair("@name", "http_requests_total"), Pair("method", "post"), Pair("code", "200"))
    grapheneMetric[0].value shouldBe 1027.0
    grapheneMetric[0].source shouldBe Source.PROMETHEUS
    grapheneMetric[0].timestampMillis() shouldBe 1580531540000
  }

  @Test
  internal fun `should deserialize from multiple prometheus metric`() {
    val grapheneMetric = prometheusDeserializer.deserialize("prometheus.topic", MULTIPLE_PROMETHEUS_METRIC.toByteArray())

    grapheneMetric shouldNotBe null

    grapheneMetric[0].id shouldBe "swap_in;host=local"
    grapheneMetric[0].tags shouldContainAll mapOf(Pair("@name", "swap_in"), Pair("host", "local"))
    grapheneMetric[0].value shouldBe 0.0
    grapheneMetric[0].source shouldBe Source.PROMETHEUS
    grapheneMetric[0].timestampMillis() shouldBe 1580532000000

    grapheneMetric[1].id shouldBe "swap_out;host=local"
    grapheneMetric[1].tags shouldContainAll mapOf(Pair("@name", "swap_out"), Pair("host", "local"))
    grapheneMetric[1].value shouldBe 0.0
    grapheneMetric[1].source shouldBe Source.PROMETHEUS
    grapheneMetric[1].timestampMillis() shouldBe 1580532000000
  }

  companion object {
    const val INVALID_PROMETHEUS_METRIC =
      """# HELP swap_in Telegraf collected metric
        # TYPE swap_in counter
        http_requests_total{method="post",code="200"} stringValue 1580531540000
      """

    const val SINGLE_PROMETHEUS_METRIC =
      """# HELP swap_in Telegraf collected metric
        # TYPE swap_in counter
        http_requests_total{method="post",code="200"} 1027 1580531540000
      """

    const val MULTIPLE_PROMETHEUS_METRIC =
      """# HELP swap_in Telegraf collected metric
        # TYPE swap_in counter
        swap_in{host="local"} 0 1580532000000
        # HELP swap_out Telegraf collected metric
        # TYPE swap_out counter
        swap_out{host="local"} 0 1580532000000
      """
  }
}
