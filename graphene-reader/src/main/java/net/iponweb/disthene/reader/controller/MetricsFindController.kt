package net.iponweb.disthene.reader.controller

import net.iponweb.disthene.reader.handler.response.HierarchyMetricPath
import net.iponweb.disthene.reader.service.index.ElasticsearchIndexService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class MetricsFindController(
  private val elasticsearchIndexService: ElasticsearchIndexService
) {

  @PostMapping("/metrics/find", consumes = [MediaType.APPLICATION_JSON_VALUE])
  fun metricsFindAboutApplicationJson(
    @RequestBody metricsFindRequest: MetricsFindRequest
  ): Set<HierarchyMetricPath> {

    return elasticsearchIndexService.getPathsAsHierarchyMetricPath("NONE", metricsFindRequest.query)
  }

  @PostMapping("/metrics/find", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
  fun metricsFindAboutFormUrlEncoded(
    @ModelAttribute("metricsFindRequest") metricsFindRequest: MetricsFindRequest
  ): Set<HierarchyMetricPath> {

    return elasticsearchIndexService.getPathsAsHierarchyMetricPath("NONE", metricsFindRequest.query)
  }

  data class MetricsFindRequest(
    val query: String,
    val from: String?,
    val to: String?
  )
}
