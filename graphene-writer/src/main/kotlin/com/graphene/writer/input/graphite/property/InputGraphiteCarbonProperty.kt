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
class InputGraphiteCarbonProperty {

  var bind: String? = null
  var port: Int = 0
  var retention: Int = 0
  var rollups: List<Rollup> = ArrayList()
    set(rollups) {
      baseRollup = rollups[0]
      field = rollups.subList(1, rollups.size)
    }
  var baseRollup: Rollup? = null
  var route: Route? = null

  @PostConstruct
  fun init() {
    logger.info("Load InputGraphiteCarbonProperty : {}", toString())
  }

  override fun toString(): String {
    return "InputGraphiteCarbonProperty{" +
      "bind='" + bind + '\''.toString() +
      ", port=" + port +
      ", rollups=" + this.rollups +
      ", baseRollup=" + baseRollup +
      '}'.toString()
  }

  class Route {
    var host: String = "127.0.0.1"

    var port: Int = 2003
  }

  companion object {

    private val logger = LoggerFactory.getLogger(InputGraphiteCarbonProperty::class.java)
  }
}
