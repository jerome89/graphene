package com.graphene.writer.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

import javax.annotation.PostConstruct
import java.util.ArrayList

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer.carbon")
class CarbonConfiguration {

  var bind: String? = null
  var port: Int = 0
  var rollups: List<Rollup> = ArrayList()
    set(rollups) {
      baseRollup = rollups[0]
      field = rollups.subList(1, rollups.size)
    }
  var baseRollup: Rollup? = null
    private set

  @PostConstruct
  fun init() {
    logger.info("Load Graphene carbon configuration : {}", toString())
  }

  override fun toString(): String {
    return "CarbonConfiguration{" +
      "bind='" + bind + '\''.toString() +
      ", port=" + port +
      ", rollups=" + this.rollups +
      ", baseRollup=" + baseRollup +
      '}'.toString()
  }

  companion object {

    private val logger = LoggerFactory.getLogger(CarbonConfiguration::class.java)
  }
}
