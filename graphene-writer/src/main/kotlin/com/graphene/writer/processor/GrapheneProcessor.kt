package com.graphene.writer.processor

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.blacklist.BlacklistService
import com.graphene.writer.event.GrapheneDataStoreEvent
import net.iponweb.disthene.service.index.IndexService
import net.iponweb.disthene.service.store.CassandraService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class GrapheneProcessor(
  val cassandraService: CassandraService,
  val indexService: IndexService,
  val blacklistService: BlacklistService,
  val applicationEventPublisher: ApplicationEventPublisher
) {

  @Async("grapheneProcessorExecutor")
  fun process(grapheneMetric: GrapheneMetric) {
    if (blacklistService.isBlackListed(grapheneMetric)) {
      return
    }

    cassandraService.handle(grapheneMetric)
    indexService.handle(grapheneMetric)

    applicationEventPublisher.publishEvent(GrapheneDataStoreEvent(grapheneMetric))
  }
}
