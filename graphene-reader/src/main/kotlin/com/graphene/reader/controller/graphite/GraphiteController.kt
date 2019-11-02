package com.graphene.reader.controller.graphite

import com.graphene.common.HierarchyMetricPaths
import com.graphene.reader.controller.graphite.request.MetricsFindRequest
import com.graphene.reader.controller.graphite.request.RenderRequest
import com.graphene.reader.handler.RenderParameters
import com.graphene.reader.store.key.ElasticsearchKeySearchHandler
import net.iponweb.disthene.reader.handler.RenderHandler
import net.iponweb.disthene.reader.utils.MetricRule
import org.apache.log4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

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
  private val elasticsearchKeySearchHandler: ElasticsearchKeySearchHandler
) {

  private val logger = Logger.getLogger(GraphiteController::class.java)

  @PostMapping("/render")
  fun postRender(
    @ModelAttribute("renderRequest") renderRequest: RenderRequest
  ): ResponseEntity<*> {
    return renderHandler.handle(RenderParameters.from(renderRequest))
  }

  @RequestMapping("/metrics/find", "/find", method = [RequestMethod.POST, RequestMethod.GET])
  fun metricsFindAboutFormUrlEncoded(
    @ModelAttribute("metricsFindRequest") metricsFindRequest: MetricsFindRequest
  ): Collection<HierarchyMetricPaths.HierarchyMetricPath> {

    return getPathsAsHierarchyMetricPath(metricsFindRequest)
  }

  private fun getPathsAsHierarchyMetricPath(metricsFindRequest: MetricsFindRequest): Collection<HierarchyMetricPaths.HierarchyMetricPath> {
    return elasticsearchKeySearchHandler.getHierarchyMetricPaths(MetricRule.defaultTenant(), metricsFindRequest.query, metricsFindRequest.from!!, metricsFindRequest.until!!)
  }
}
