package com.graphene.writer.processor

import com.graphene.writer.blacklist.BlacklistService
import com.graphene.writer.event.GrapheneDataStoreEvent
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.StoreHandler
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class GrapheneProcessor(
  val blacklistService: BlacklistService,
  val applicationEventPublisher: ApplicationEventPublisher,
  val storeHandlers: List<StoreHandler>
) {

  @Async("grapheneProcessorExecutor")
  fun process(grapheneMetric: GrapheneMetric) {
    if (blacklistService.isBlackListed(grapheneMetric)) {
      return
    }

    for (storeHandler in storeHandlers) {
      storeHandler.handle(grapheneMetric)
    }

    applicationEventPublisher.publishEvent(GrapheneDataStoreEvent(grapheneMetric))
  }
}
