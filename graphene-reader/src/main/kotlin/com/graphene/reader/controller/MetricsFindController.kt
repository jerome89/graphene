package com.graphene.reader.controller

import net.iponweb.disthene.reader.handler.response.HierarchyMetricPath
import net.iponweb.disthene.reader.service.index.ElasticsearchIndexService
import org.apache.log4j.Logger
import org.springframework.web.bind.annotation.*

@RestController
class MetricsFindController(
  private val elasticsearchIndexService: ElasticsearchIndexService
) {

  private val logger = Logger.getLogger(MetricsFindController::class.java)

  @PostMapping("/metrics/find")
  fun metricsFindAboutFormUrlEncoded(
    @ModelAttribute("metricsFindRequest") metricsFindRequest: MetricsFindRequest
  ): Set<HierarchyMetricPath> {

    return getPathsAsHierarchyMetricPath(metricsFindRequest)
  }

  private fun getPathsAsHierarchyMetricPath(metricsFindRequest: MetricsFindRequest): Set<HierarchyMetricPath> {
    return elasticsearchIndexService.getPathsAsHierarchyMetricPath("NONE", metricsFindRequest.query)
  }

  data class MetricsFindRequest(
    val query: String,
    val from: String?,
    val to: String?
  )
}
