package com.graphene.writer.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer")
class GrapheneWriterProperty(
  var statsProperty: StatsProperty
) {

  override fun toString(): String {
    return "GrapheneWriterConfiguration{" +
      ", statsProperty=" + statsProperty +
      '}'.toString()
  }
}
