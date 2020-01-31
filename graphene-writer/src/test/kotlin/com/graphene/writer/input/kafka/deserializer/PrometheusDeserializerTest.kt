package com.graphene.writer.input.kafka.deserializer

import com.graphene.writer.input.Source
import io.kotlintest.matchers.maps.shouldContainAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.Test

internal class PrometheusDeserializerTest {

  private val prometheusDeserializer = PrometheusDeserializer()

  @Test
  internal fun `should be null if message is HELP`() {
    val grapheneMetric = prometheusDeserializer.deserialize("prometheus.topic", PROMETHEUS_METRIC_HINT.toByteArray())

    grapheneMetric shouldBe null
  }

  @Test
  internal fun `should be null if message is TYPE`() {
    val grapheneMetric = prometheusDeserializer.deserialize("prometheus.topic", PROMETHEUS_METRIC_TYPE.toByteArray())

    grapheneMetric shouldBe null
  }

  @Test
  internal fun `should deserialize from byte array`() {
    val grapheneMetric = prometheusDeserializer.deserialize("prometheus.topic", PROMETHEUS_METRIC.toByteArray())

    grapheneMetric shouldNotBe null

    grapheneMetric!!.id shouldBe "http_requests_total;method=post&code=200"
    grapheneMetric.tags shouldContainAll mapOf(Pair("@name", "http_requests_total"), Pair("method", "post"), Pair("code", "200"))
    grapheneMetric.value shouldBe 1027.0
    grapheneMetric.source shouldBe Source.PROMETHEUS
    grapheneMetric.timestampMillis() shouldBe 1395066363000
  }

  companion object {
    const val PROMETHEUS_METRIC_HINT = "# HELP http_requests_total The total number of HTTP requests."
    const val PROMETHEUS_METRIC_TYPE = "# TYPE http_requests_total counter"
    const val PROMETHEUS_METRIC = """http_requests_total{method="post",code="200"} 1027 1395066363000"""
  }
}
