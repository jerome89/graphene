package com.graphene.reader.controller.graphite

import com.graphene.common.HierarchyMetricPaths
import com.graphene.reader.controller.graphite.request.MetricsFindRequest
import com.graphene.reader.controller.graphite.request.RenderRequest
import com.graphene.reader.handler.RenderHandler
import com.graphene.reader.handler.RenderParameters
import com.graphene.reader.store.key.SimpleKeySearchHandler
import com.graphene.reader.utils.MetricRule
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
  private val simpleKeySearchHandler: SimpleKeySearchHandler
) {

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
    return simpleKeySearchHandler.getHierarchyMetricPaths(
      MetricRule.defaultTenant(),
      metricsFindRequest.query,
      metricsFindRequest.fromMillis(),
      metricsFindRequest.untilMillis()
    )
  }
}
