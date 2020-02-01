package com.graphene.writer.input.kafka.deserializer

import com.graphene.common.rule.GrapheneRules.SpecialChar
import com.graphene.common.utils.DateTimeUtils
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

    val grapheneMetrics = mutableListOf<GrapheneMetric>()

    try {
      val plainPrometheusMetrics = String(data!!).split("\n")
      for (i in plainPrometheusMetrics.indices step 1) {
        if (startsWithHash(plainPrometheusMetrics[i])) {
          continue
        }

        newGrapheneMetric(plainPrometheusMetrics[i])?.run {
          grapheneMetrics.add(this)
        }
      }
    } catch (e: Throwable) {
      val convertedData = data?.run { String(this) }
      log.error("Fail to deserialize from prometheus format metric to graphene metric : $convertedData", e)
    }

    return grapheneMetrics
  }

  private fun startsWithHash(plainPrometheusMetric: String): Boolean {
    return plainPrometheusMetric.isNotEmpty() && plainPrometheusMetric[0] == SpecialChar.HASH
  }

  private fun newGrapheneMetric(plainPrometheusMetric: String): GrapheneMetric? {
    if (plainPrometheusMetric.isEmpty()) {
      return null
    }

    try {
      val tags = TreeMap<String, String>()
      var value = ""

      val tmp = StringBuilder()
      val id = StringBuilder()
      var tmpTagKey = ""

      var withoutTimestamp = true

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
        } else if (metricChar == SpecialChar.WHITESPACE) {
          if (tmp.isEmpty()) {
            continue
          }

          value = tmp.toString()
          withoutTimestamp = false
          tmp.clear()
        } else if (metricChar != SpecialChar.DOUBLE_QUOTE) {
          tmp.append(metricChar)
        }
      }

      var timestamp: String
      // your prometheus format metric hasn't timestamp
      if (withoutTimestamp) {
        value = tmp.toString()
        timestamp = DateTimeUtils.currentTimeMillis().toString()
      } else {
        timestamp = tmp.toString()
      }

      return GrapheneMetric(Source.PROMETHEUS, id.toString(), mutableMapOf(), tags, TreeMap(), value.toDouble(), normalizedTimestamp(timestamp.toLong() / 1000))
    } catch (e: Throwable) {
      log.error("Fail to deserialize from prometheus format metric to graphene metric : $plainPrometheusMetric", e)
    }

    return null
  }

  private fun normalizedTimestamp(timestamp: Long): Long {
    return timestamp / 60 * 60
  }
}
