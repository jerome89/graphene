package com.graphene.writer.input.kafka.deserializer

import com.graphene.writer.input.GrapheneMetric
import org.apache.kafka.common.serialization.Deserializer

class PrometheusDeserializer : Deserializer<GrapheneMetric> {

  override fun deserialize(topic: String?, data: ByteArray?): GrapheneMetric? {
    return null
  }
}
