package com.graphene.writer.store.key.handler

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.KeyStoreHandler
import org.apache.log4j.Logger

/**
 *
 * @author dark
 */
class LoggingKeyStoreHandler : KeyStoreHandler {

  private val log = Logger.getLogger(LoggingKeyStoreHandler::class.java)

  override fun handle(grapheneMetric: GrapheneMetric) {
    log.info("Logging : $grapheneMetric")
  }

  override fun close() {
    log.info("Close LoggingKeyStoreHandler")
  }
}
