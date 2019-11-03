package com.graphene.writer.store.key

import com.graphene.common.key.RotationProperty
import com.graphene.common.rule.GrapheneRules
import com.graphene.writer.store.KeyStoreHandler
import com.graphene.writer.store.StoreHandler
import com.graphene.writer.store.key.handler.IndexBasedKeyStoreHandler
import com.graphene.writer.store.key.handler.LoggingKeyStoreHandler
import com.graphene.writer.store.key.handler.SimpleKeyStoreHandler
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.support.GenericApplicationContext
import org.springframework.stereotype.Component

@Component
class StoreHandlerFactory(
  var keyStoreHandlersProperty: KeyStoreHandlersProperty,
  var applicationContext: GenericApplicationContext,
  var dataStoreHandlers: List<StoreHandler>
) {

  private val log = LoggerFactory.getLogger(StoreHandlerFactory::class.java)

  fun keyStoreHandlers(): List<KeyStoreHandler> {
    val keyStoreHandlers = mutableListOf<KeyStoreHandler>()
    for (handler in keyStoreHandlersProperty.handlers) {
      log.info("StoreHandlerFactory Key : ${handler.key} / Value : ${handler.value}")

      when (keyStoreHandlersProperty.handlers[handler.key]!!.handler["type"]) {
        "LoggingKeyStoreHandler" -> keyStoreHandlers.add(createLoggingKeyStoreHandler(handler.key, handler.value))
        "SimpleKeyStoreHandler" -> keyStoreHandlers.add(createSimpleKeyStoreHandler(handler.key, handler.value))
        "IndexBasedKeyStoreHandler" -> keyStoreHandlers.add(createIndexBasedKeyStoreHandler(handler.key, handler.value))
      }
    }

    return keyStoreHandlers
  }

  private fun createIndexBasedKeyStoreHandler(handlerId: HandlerId, keyStoreHandlerProperty: KeyStoreHandlerProperty): KeyStoreHandler {
    log.info("StoreHandlerFactory registers indexBasedKeyStoreHandler : $handlerId")

    return IndexBasedKeyStoreHandler(
      applicationContext.getBean("elasticsearchClientFactory") as ElasticsearchClientFactory,
      keyStoreHandlerProperty
    )
  }

  private fun createSimpleKeyStoreHandler(handlerId: HandlerId, keyStoreHandlerProperty: KeyStoreHandlerProperty): KeyStoreHandler {
    log.info("StoreHandlerFactory registers simpleKeyStoreHandler : $handlerId")

    return SimpleKeyStoreHandler(
      applicationContext.getBean("elasticsearchClientFactory") as ElasticsearchClientFactory,
      keyStoreHandlerProperty
    )
  }

  fun dataStoreHandlers(): List<StoreHandler> {
    return dataStoreHandlers
  }

  private fun createLoggingKeyStoreHandler(handlerId: HandlerId, value: KeyStoreHandlerProperty): KeyStoreHandler {
    log.info("StoreHandlerFactory Register loggingKeyStoreHandler : $handlerId")

    return LoggingKeyStoreHandler()
  }
}

@ConfigurationProperties(prefix = "graphene.writer.store.key")
data class KeyStoreHandlersProperty(
  var handlers: Map<HandlerId, KeyStoreHandlerProperty>
)

data class KeyStoreHandlerProperty(
  var tenant: String = GrapheneRules.DEFAULT_TENANT,
  var rotation: RotationProperty = RotationProperty(),
  var handler: Map<String, Any> = mapOf()
)

typealias HandlerId = String
