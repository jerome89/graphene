package com.graphene.writer.input.graphite.property

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "graphene.writer.input.graphite")
class GraphiteInputProperty(
  var carbon: CarbonProperty
) {

  @PostConstruct
  fun init() {
    logger.info("Load Graphene graphite input configuration : {}", toString())
  }

  override fun toString(): String {
    return "GraphiteInputProperty{carbon=$carbon}"
  }

  companion object {

    private val logger = LoggerFactory.getLogger(GraphiteInputProperty::class.java)
  }
}
