package com.graphene.writer.input.graphite.property

import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "graphene.writer.input.graphite")
class InputGraphiteProperty(
  var inputGraphiteCarbonProperty: InputGraphiteCarbonProperty
) {

  @PostConstruct
  fun init() {
    logger.info("Load InputGraphiteProperty : {}", toString())
  }

  override fun toString(): String {
    return "GraphiteInputProperty{InputGraphiteProperty=$inputGraphiteCarbonProperty}"
  }

  companion object {

    private val logger = LoggerFactory.getLogger(InputGraphiteProperty::class.java)
  }
}
