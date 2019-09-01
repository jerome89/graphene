package net.iponweb.disthene.reader.controller

import net.iponweb.disthene.reader.handler.response.HierarchyMetricPath
import net.iponweb.disthene.reader.service.index.ElasticsearchIndexService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MetricsFindController(
  private val elasticsearchIndexService: ElasticsearchIndexService
) {

  @GetMapping("/metrics/find")
  fun metricsFind(
    @RequestParam query: String?,
    @RequestParam(required = false) from: String?,
    @RequestParam(required = false) until: String?
  ): Set<HierarchyMetricPath> {

    return elasticsearchIndexService.getPathsAsHierarchyMetricPath("NONE", query)
  }
}
