package com.graphene.writer.blacklist

import java.util.HashMap
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer")
class BlacklistConfiguration {

  private val logger = LoggerFactory.getLogger(BlacklistConfiguration::class.java)

  var blacklist: Map<String, List<String>> = HashMap()

  @PostConstruct
  fun init() {
    logger.info("Load Graphene blacklist configuration : {}", toString())
  }

  override fun toString(): String {
    return "BlackListConfiguration{" +
      "rules=" + blacklist +
      '}'.toString()
  }
}
