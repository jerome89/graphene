package com.graphene.writer.processor

import com.graphene.writer.event.MetricStoreEvent
import com.graphene.writer.input.GrapheneMetric
import net.iponweb.disthene.service.blacklist.BlacklistService
import net.iponweb.disthene.service.index.IndexService
import net.iponweb.disthene.service.stats.StatsService
import net.iponweb.disthene.service.store.CassandraService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class GrapheneProcessor(
  val statsService: StatsService,
  val cassandraService: CassandraService,
  val indexService: IndexService,
  val blacklistService: BlacklistService
) {

  @Async("grapheneProcessorExecutor")
  fun process(grapheneMetric: GrapheneMetric) {
    if (blacklistService.isBlackListed(grapheneMetric)) {
      return
    }

    statsService.handle(MetricStoreEvent(grapheneMetric))
    cassandraService.handle(grapheneMetric)
    indexService.handle(grapheneMetric)
  }
}
