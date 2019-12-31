package com.graphene.writer.input.influxdb

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.MetricConverter
import com.graphene.writer.input.Source
import com.graphene.writer.input.UnexpectedConverterException
import java.util.TreeMap
import java.util.concurrent.TimeUnit

/**
 *
 * This class converts InfluxDB's plain metric format to graphene metric format.
 * below links details about InfluxDB's plain metric format:
 * https://docs.influxdata.com/influxdb/v1.7/write_protocols/line_protocol_tutorial/
 *
 * @author dark
 * @since 1.5.0
 */
class InfluxDbMetricConverter : MetricConverter<String> {

  override fun convert(metric: String): GrapheneMetric {
    try {
      var stage = ConvertStage.MEASUREMENT

      var tmpRegistry = StringBuilder()
      var tmpKey: String? = null
      val grapheneMetric = GrapheneMetric(Source.INFLUXDB, mutableMapOf(), TreeMap(), mutableMapOf(), 1L)

      for (index in metric.withIndex()) {
        var char = index.value

        when (stage) {
          ConvertStage.MEASUREMENT -> {
            if (char == ',') {
              grapheneMetric.putMeta("@measurement", tmpRegistry.toString())
              tmpRegistry.clear()
              stage = ConvertStage.TAG_KEY
            }
          }
          ConvertStage.TAG_KEY -> {
            if (char == '=') {
              tmpKey = tmpRegistry.toString()
              tmpRegistry.clear()
              stage = ConvertStage.TAG_VALUE
            }
          }
          ConvertStage.TAG_VALUE -> {
            if (char == ',') {
              grapheneMetric.putTag(tmpKey!!, tmpRegistry.toString())
              tmpKey = null
              tmpRegistry.clear()
              stage = ConvertStage.TAG_KEY
            }

            if (char == ' ') {
              grapheneMetric.putTag(tmpKey!!, tmpRegistry.toString())
              tmpKey = null
              tmpRegistry.clear()
              stage = ConvertStage.FIELD_KEY
            }
          }
          ConvertStage.FIELD_KEY -> {
            if (char == '=') {
              tmpKey = tmpRegistry.toString()
              tmpRegistry.clear()
              stage = ConvertStage.FIELD_VALUE
            }
          }
          ConvertStage.FIELD_VALUE -> {
            if (char == ',') {
              grapheneMetric.putMetrics(tmpKey!!, toDouble(tmpRegistry))
              tmpKey = null
              tmpRegistry.clear()
              stage = ConvertStage.FIELD_KEY
            }

            if (char == ' ') {
              grapheneMetric.putMetrics(tmpKey!!, toDouble(tmpRegistry))
              tmpKey = null
              tmpRegistry.clear()
              stage = ConvertStage.TIMESTAMP
            }
          }
          ConvertStage.TIMESTAMP -> {
            grapheneMetric.timestampSeconds = TimeUnit.NANOSECONDS.toSeconds(metric.substring(index.index).toLong())
            return grapheneMetric
          }
        }

        if (char != ' ' && char != '=' && char != ',') {
          tmpRegistry.append(char)
        }
      }

      return grapheneMetric
    } catch (e: Exception) {
      throw UnexpectedConverterException("Fail to convert InfluxDB metric", e)
    }
  }

  private fun toDouble(tmpRegistry: StringBuilder): Double {
    var value = if (tmpRegistry.last() == 'i') {
      tmpRegistry.substring(0, tmpRegistry.lastIndex)
    } else {
      tmpRegistry.toString()
    }

    return value.toDouble()
  }
}
