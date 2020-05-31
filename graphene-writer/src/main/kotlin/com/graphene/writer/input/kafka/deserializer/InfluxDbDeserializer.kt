package com.graphene.writer.input.kafka.deserializer

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.MetricConverter
import com.graphene.writer.input.influxdb.InfluxDbMetricConverter
import java.util.Objects
import org.apache.kafka.common.serialization.Deserializer

class InfluxDbDeserializer : Deserializer<List<GrapheneMetric>> {

  private val influxDbMetricConverter: MetricConverter<String> = InfluxDbMetricConverter()

  override fun deserialize(topic: String?, data: ByteArray?): List<GrapheneMetric> {
    if (Objects.isNull(data)) {
      return emptyList()
    }

    return influxDbMetricConverter.convert(String(data!!))
  }
}
