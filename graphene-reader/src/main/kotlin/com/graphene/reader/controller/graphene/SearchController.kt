package com.graphene.reader.controller.graphene

import com.graphene.reader.service.index.ElasticsearchIndexService
import net.iponweb.disthene.reader.service.stats.StatsService
import org.springframework.web.bind.annotation.*

@RestController
class SearchController(
  private val statsService: StatsService,
  private val elasticsearchIndexService: ElasticsearchIndexService
) {

  @GetMapping("/search")
  fun getPathsStats(
    @RequestParam tenant: String = "NONE",
    @RequestParam query: String
  ): String {
    statsService.incPathsRequests(tenant)
    return elasticsearchIndexService.getSearchPathsAsString(tenant, query, SEARCH_LIMIT)
  }

  @PostMapping("/search")
  fun postPathsStats(@RequestBody parameters: SearchParameters): String {
    statsService.incPathsRequests(parameters.tenant)
    return elasticsearchIndexService.getSearchPathsAsString(parameters.tenant, parameters.query, SEARCH_LIMIT)
  }

  companion object {
    const val SEARCH_LIMIT = 100
  }

  data class SearchParameters(
    var tenant: String = "NONE",
    var query: String
  )
}