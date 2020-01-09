package com.graphene.writer.input.kafka.deserializer

import com.graphene.writer.error.Errors
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.graphite.GraphiteMetric
import com.graphene.writer.input.graphite.GraphiteMetricConverter
import io.netty.util.CharsetUtil
import org.apache.kafka.common.serialization.Deserializer
import java.util.Objects

class GraphiteDeserializer : Deserializer<GrapheneMetric> {

  override fun deserialize(topic: String?, data: ByteArray?): GrapheneMetric {
    if (Objects.isNull(data)) {
      throw Errors.ILLEGAL_ARGUMENT_EXCEPTION.exception()
    }

    val metric = data!!.toString(CharsetUtil.UTF_8).trim { it <= ' ' }

    val metricWithIndex = metric.split(" ")

    val graphiteMetric = GraphiteMetric(metricWithIndex[0], metricWithIndex[1].toDouble(), metricWithIndex[2].toLong())
    val graphiteMetricConverter = GraphiteMetricConverter()

    return graphiteMetricConverter.convert(graphiteMetric)[0]
  }
}
