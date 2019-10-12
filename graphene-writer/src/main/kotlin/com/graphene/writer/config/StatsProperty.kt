package com.graphene.writer.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

import javax.annotation.PostConstruct
import java.net.InetAddress
import java.net.UnknownHostException

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer.stats")
class StatsProperty {

  var interval: Int = 0
  var tenant: String? = null
  var hostname: String? = null
  var isLog: Boolean = false

  @PostConstruct
  fun init() {
    logger.info("Load Graphene stats configuration : {}", toString())
  }

  init {
    try {
      hostname = InetAddress.getLocalHost().hostName
    } catch (e: UnknownHostException) {
      hostname = "unknown"
    }

  }

  override fun toString(): String {
    return "StatsProperty{" +
      "interval=" + interval +
      ", tenant='" + tenant + '\''.toString() +
      ", hostname='" + hostname + '\''.toString() +
      ", log=" + isLog +
      '}'.toString()
  }

  companion object {

    private val logger = LoggerFactory.getLogger(StatsProperty::class.java)
  }
}
