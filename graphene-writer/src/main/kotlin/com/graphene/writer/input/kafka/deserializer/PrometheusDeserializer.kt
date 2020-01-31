package com.graphene.writer.input.kafka.deserializer

import com.graphene.common.rule.GrapheneRules
import com.graphene.common.rule.GrapheneRules.SpecialChar
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.Source
import org.apache.kafka.common.serialization.Deserializer
import java.util.Objects
import java.util.TreeMap

class PrometheusDeserializer : Deserializer<GrapheneMetric> {

  override fun deserialize(topic: String?, data: ByteArray?): GrapheneMetric? {
    if (Objects.isNull(data)) {
      return null
    }

    val plainTextMetric = String(data!!)
    if (plainTextMetric.startsWith(SpecialChar.HASH)) {
      return null
    }

    val tags = TreeMap<String, String>()
    var value = ""
    var timestamp: String

    val tmp = StringBuilder()
    val id = StringBuilder()
    var tmpTagKey = ""

    val metricChars = plainTextMetric.toCharArray()
    for (metricChar in metricChars) {
      if (metricChar == SpecialChar.BRACE_OPEN) {
        tags[GrapheneRules.METRIC_NAME_TAG] = tmp.toString()
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
          id.append(SpecialChar.AND)
        }
      } else if (metricChar == SpecialChar.WHITESPACE && tmp.isNotEmpty()) {
        value = tmp.toString()
        tmp.clear()
      } else if (metricChar != SpecialChar.DOUBLE_QUOTE){
        tmp.append(metricChar)
      }
    }

    timestamp = tmp.toString()

    return GrapheneMetric(Source.PROMETHEUS, id.toString(), mutableMapOf(), tags, TreeMap(), value.toDouble(), timestamp.toLong() / 1000)
  }
}
