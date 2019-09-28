package com.graphene.writer.config

import net.iponweb.disthene.service.aggregate.CarbonConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer")
class GrapheneWriterConfiguration(
  var carbon: CarbonConfiguration,
  var store: StoreConfiguration,
  var index: IndexConfiguration,
  var stats: StatsConfiguration
) {

  override fun toString(): String {
    return "GrapheneWriterConfiguration{" +
      "carbon=" + carbon +
      ", store=" + store +
      ", index=" + index +
      ", stats=" + stats +
      '}'.toString()
  }
}
