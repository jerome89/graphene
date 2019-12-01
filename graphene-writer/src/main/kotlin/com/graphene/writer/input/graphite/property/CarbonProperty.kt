package com.graphene.writer.input.graphite.property

import com.graphene.writer.config.Rollup
import java.util.ArrayList
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer.input.graphite.carbon")
class CarbonProperty {

  var bind: String? = null
  var port: Int = 0
  var retention: Int = 0
  var rollups: List<Rollup> = ArrayList()
    set(rollups) {
      baseRollup = rollups[0]
      field = rollups.subList(1, rollups.size)
    }
  var baseRollup: Rollup? = null

  @PostConstruct
  fun init() {
    logger.info("Load Graphene carbon configuration : {}", toString())
  }

  override fun toString(): String {
    return "CarbonProperty{" +
      "bind='" + bind + '\''.toString() +
      ", port=" + port +
      ", rollups=" + this.rollups +
      ", baseRollup=" + baseRollup +
      '}'.toString()
  }

  companion object {

    private val logger = LoggerFactory.getLogger(CarbonProperty::class.java)
  }
}
