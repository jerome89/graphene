package com.graphene.reader.controller.graphite

import com.graphene.reader.controller.graphite.request.MetricsFindRequest
import com.graphene.reader.controller.graphite.request.RenderRequest
import com.graphene.reader.handler.RenderParameters
import net.iponweb.disthene.reader.handler.RenderHandler
import com.graphene.common.HierarchyMetricPaths
import com.graphene.reader.service.index.ElasticsearchIndexService
import net.iponweb.disthene.reader.utils.MetricRule
import org.apache.log4j.Logger
import org.springframework.web.bind.annotation.*
import java.nio.charset.StandardCharsets

/**
 *
 * This controller provides Graphite api like below:
 * https://graphite-api.readthedocs.io/en/latest/api.html
 *
 * @since 1.0.0
 * @author dark
 */
@RestController
class GraphiteController(
  private val renderHandler: RenderHandler,
  private val elasticsearchIndexService: ElasticsearchIndexService
) {

  private val logger = Logger.getLogger(GraphiteController::class.java)

  @PostMapping("/render")
  fun postRender(
    @ModelAttribute("renderRequest") renderRequest: RenderRequest
  ): String {
    val response = renderHandler.handle(RenderParameters.from(renderRequest))
    return String(response.content().array(), StandardCharsets.UTF_8)
  }

  @RequestMapping("/metrics/find", "/find", method = [RequestMethod.POST, RequestMethod.GET])
  fun metricsFindAboutFormUrlEncoded(
    @ModelAttribute("metricsFindRequest") metricsFindRequest: MetricsFindRequest
  ): Collection<HierarchyMetricPaths.HierarchyMetricPath> {

    return getPathsAsHierarchyMetricPath(metricsFindRequest)
  }

  private fun getPathsAsHierarchyMetricPath(metricsFindRequest: MetricsFindRequest): Collection<HierarchyMetricPaths.HierarchyMetricPath> {
    return elasticsearchIndexService.getHierarchyMetricPaths(MetricRule.defaultTenant(), metricsFindRequest.query)
  }
}
