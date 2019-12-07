package com.graphene.writer.store.key.handler

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.KeyStoreHandler
import org.apache.logging.log4j.LogManager

/**
 *
 * @author dark
 */
class LoggingKeyStoreHandler : KeyStoreHandler {

  private val log = LogManager.getLogger(LoggingKeyStoreHandler::class.java)

  override fun handle(grapheneMetric: GrapheneMetric) {
    log.info("Logging : $grapheneMetric")
  }

  override fun close() {
    log.info("Close LoggingKeyStoreHandler")
  }
}
