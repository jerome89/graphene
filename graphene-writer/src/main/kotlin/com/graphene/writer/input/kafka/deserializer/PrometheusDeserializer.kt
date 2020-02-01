package com.graphene.writer.input.kafka.deserializer

import com.graphene.common.rule.GrapheneRules.SpecialChar
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.Source
import java.util.Objects
import java.util.TreeMap
import org.apache.kafka.common.serialization.Deserializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class PrometheusDeserializer : Deserializer<List<GrapheneMetric>> {

  private val log: Logger = LogManager.getLogger(javaClass)

  override fun deserialize(topic: String?, data: ByteArray?): List<GrapheneMetric> {
    if (Objects.isNull(data)) {
      return emptyList()
    }

    val plainPrometheusMetrics = String(data!!).split("\n")

    val grapheneMetrics = mutableListOf<GrapheneMetric>()
    for (i in 2..plainPrometheusMetrics.size step 3) {
      newGrapheneMetric(plainPrometheusMetrics[i])?.run {
        grapheneMetrics.add(this)
      }
    }

    return grapheneMetrics
  }

  private fun newGrapheneMetric(plainPrometheusMetric: String): GrapheneMetric? {
    try {
      val tags = TreeMap<String, String>()
      var value = ""
      var timestamp: String

      val tmp = StringBuilder()
      val id = StringBuilder()
      var tmpTagKey = ""

      val metricChars = plainPrometheusMetric.toCharArray()
      for (metricChar in metricChars) {
        if (metricChar == SpecialChar.BRACE_OPEN) {
          id.append("$tmp;")
          tmp.clear()
        } else if (metricChar == SpecialChar.EQUAL) {
          tmpTagKey = tmp.toString()
          tmp.clear()
        } else if (metricChar == SpecialChar.COMMA || metricChar == SpecialChar.BRACE_CLOSE) {
          tags[tmpTagKey] = tmp.toString()
          id.append("$tmpTagKey=$tmp")
          tmp.clear()

          if (metricChar == SpecialChar.COMMA) {
            id.append(SpecialChar.SEMICOLON)
          }
        } else if (metricChar == SpecialChar.WHITESPACE && tmp.isNotEmpty()) {
          value = tmp.toString()
          tmp.clear()
        } else if (metricChar != SpecialChar.DOUBLE_QUOTE) {
          tmp.append(metricChar)
        }
      }

      timestamp = tmp.toString()

      return GrapheneMetric(Source.PROMETHEUS, id.toString(), mutableMapOf(), tags, TreeMap(), value.toDouble(), timestamp.toLong() / 1000)
    } catch (e: Throwable) {
      log.error("Fail to deserialize from prometheus format metric to graphene metric : $plainPrometheusMetric")
    }

    return null
  }
}
