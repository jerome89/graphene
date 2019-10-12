package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.KeyStoreHandler
import org.apache.log4j.Logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

/**
 *
 * @author dark
 */
@Component
@ConditionalOnProperty(prefix = "graphene.writer.store.key.handlers.logging-key-store-handler", name = ["enabled"], havingValue = "true")
class LoggingKeyStoreHandler : KeyStoreHandler {

  private val log = Logger.getLogger(LoggingKeyStoreHandler::class.java)

  override fun handle(grapheneMetric: GrapheneMetric) {
    log.info("Logging : $grapheneMetric")
  }
}
