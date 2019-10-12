package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.KeyStoreHandler
import com.graphene.writer.store.key.model.IndexBasedKeyStoreHandlerProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

/**
 *
 * @author dark
 */
@Component
@ConditionalOnProperty(prefix = "graphene.writer.store.key.handlers.index-based-key-store-handler", name = ["enabled"], havingValue = "true")
class IndexBasedKeyStoreHandler(
  var property: IndexBasedKeyStoreHandlerProperty
) : KeyStoreHandler {

  override fun handle(grapheneMetric: GrapheneMetric) {

  }

}
