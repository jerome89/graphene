package net.iponweb.disthene.reader.controller

import net.iponweb.disthene.reader.service.index.ElasticsearchIndexService
import net.iponweb.disthene.reader.service.stats.StatsService
import org.springframework.web.bind.annotation.*

@RestController
class PathsController(
  private val elasticsearchIndexService: ElasticsearchIndexService,
  private val statsService: StatsService
) {

  @PostMapping("/paths")
  fun postPaths(@RequestBody parameters: PathsParameters): String {

    statsService.incPathsRequests(parameters.tenant)
    return elasticsearchIndexService.getPathsAsJsonArray(parameters.tenant, parameters.query)
  }

  @GetMapping("/paths")
  fun getPaths(
    @RequestParam(defaultValue = "NONE") tenant: String,
    @RequestParam query: String
    ): String {

    statsService.incPathsRequests(tenant)
    return elasticsearchIndexService.getPathsAsJsonArray(tenant, query)
  }

  data class PathsParameters(
    var tenant: String? = "NONE",
    var query: String
  )
}
