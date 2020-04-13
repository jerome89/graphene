package com.graphene.writer.processor

import com.graphene.writer.blacklist.BlacklistService
import com.graphene.writer.event.GrapheneDataStoreEvent
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.StoreHandler
import com.graphene.writer.store.key.StoreHandlerFactory
import org.apache.logging.log4j.LogManager
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Component
class GrapheneProcessor(
  val blacklistService: BlacklistService,
  val applicationEventPublisher: ApplicationEventPublisher,
  val storeHandlerFactory: StoreHandlerFactory
) {

  val log = LogManager.getLogger(javaClass)
  var storeHandlers: MutableList<StoreHandler> = mutableListOf()

  @PostConstruct
  fun init() {
    storeHandlers.addAll(storeHandlerFactory.keyStoreHandlers())
    storeHandlers.addAll(storeHandlerFactory.dataStoreHandlers())
  }

  @Async("grapheneProcessorExecutor")
  fun process(grapheneMetric: GrapheneMetric) {
    if (blacklistService.isBlackListed(grapheneMetric)) {
      return
    }

    val metricBytes = grapheneMetric.toString().toByteArray()
    if (HARD_CODING_LIMIT < metricBytes.size) {
      log.warn("Graphene has a metric limit of 512 bytes. But it is ${metricBytes.size} bytes. Drop the graphene metric : $grapheneMetric.")
      return
    }

    for (storeHandler in storeHandlers) {
      storeHandler.handle(grapheneMetric)
    }

    applicationEventPublisher.publishEvent(GrapheneDataStoreEvent(grapheneMetric))
  }

  @PreDestroy
  fun destroy() {
    for (storeHandler in storeHandlers) {
      storeHandler.close()
    }
  }

  companion object {
    const val HARD_CODING_LIMIT = 512
  }
}
