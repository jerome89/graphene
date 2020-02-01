package com.graphene.writer.input.kafka.deserializer

import com.graphene.writer.error.Errors
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.graphite.GraphiteMetric
import com.graphene.writer.input.graphite.GraphiteMetricConverter
import io.netty.util.CharsetUtil
import java.util.Objects
import org.apache.kafka.common.serialization.Deserializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class GraphiteDeserializer : Deserializer<List<GrapheneMetric>> {

  private val log: Logger = LogManager.getLogger(javaClass)
  private val graphiteMetricConverter = GraphiteMetricConverter()

  override fun deserialize(topic: String?, data: ByteArray?): List<GrapheneMetric> {
    try {
      if (Objects.isNull(data)) {
        throw Errors.ILLEGAL_ARGUMENT_EXCEPTION.exception()
      }

      val metric = data!!.toString(CharsetUtil.UTF_8).trim { it <= ' ' }
      val metricWithIndex = metric.split(" ")
      val graphiteMetric = GraphiteMetric(metricWithIndex[0], metricWithIndex[1].toDouble(), metricWithIndex[2].toLong())
      return listOf(graphiteMetricConverter.convert(graphiteMetric)[0])
    } catch (e: Throwable) {
      log.error("Fail to deserialize from graphite format metric to graphene metric : $data")
    }

    return emptyList()
  }
}
