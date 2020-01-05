package com.graphene.writer.input.graphite

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class GraphiteMetric @JsonCreator constructor (
  @param:JsonProperty("key") val key: String,
  @param:JsonProperty("value") val value: Double,
  @param:JsonProperty("timestamp ")val timestamp: Long
) {
  companion object {
    const val DOT = "."
  }
}
