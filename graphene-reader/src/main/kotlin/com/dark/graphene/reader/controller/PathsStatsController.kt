package com.dark.graphene.reader.controller

import net.iponweb.disthene.reader.service.index.ElasticsearchIndexService
import net.iponweb.disthene.reader.service.stats.StatsService
import org.springframework.web.bind.annotation.*

@RestController
class PathsStatsController(
  private val elasticsearchIndexService: ElasticsearchIndexService,
  private val statsService: StatsService
) {

  @GetMapping("/path_stats")
  fun getPathsStats(
    @RequestParam tenant: String? = "NONE",
    @RequestParam query: String
  ): String {
    statsService.incPathsRequests(tenant)
    return elasticsearchIndexService.getPathsWithStats(tenant, query)
  }

  @PostMapping("/path_stats")
  fun postPathsStats(@RequestBody parameters: PathStatsParameters): String {
    statsService.incPathsRequests(parameters.tenant)
    return elasticsearchIndexService.getPathsWithStats(parameters.tenant, parameters.query)
  }

  data class PathStatsParameters(
    var tenant: String? = "NONE",
    var query: String
  )
}