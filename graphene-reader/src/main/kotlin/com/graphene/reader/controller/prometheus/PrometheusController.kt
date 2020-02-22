package com.graphene.reader.controller.prometheus

import com.graphene.reader.controller.graphite.request.RenderRequest
import com.graphene.reader.controller.prometheus.request.QueryRangeRequest
import com.graphene.reader.controller.prometheus.request.QueryRequest
import com.graphene.reader.controller.prometheus.response.PrometheusResponse
import com.graphene.reader.controller.prometheus.response.QueryRangeResponse
import com.graphene.reader.controller.prometheus.response.QueryResponse
import com.graphene.reader.controller.prometheus.response.QueryResponseData
import com.graphene.reader.handler.RenderHandler
import com.graphene.reader.handler.RenderParameters
import com.graphene.reader.handler.prometheus.PrometheusRenderHandler
import com.graphene.reader.service.index.KeySearchHandler
import com.graphene.reader.service.tag.TagSearchHandler
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
 * @since 2.0.0
 * @author jerome89
 */
@RestController
@RequestMapping("/prometheus/api/v1")
class PrometheusController(
  private val renderHandler: RenderHandler,
  private val prometheusRenderHandler: PrometheusRenderHandler,
  private val keySearchHandler: KeySearchHandler,
  private val tagSearchHandler: TagSearchHandler
) {

  @PostMapping("/render")
  fun postRender(
    @ModelAttribute("renderRequest") renderRequest: RenderRequest
  ): ResponseEntity<*> {
    return renderHandler.handle(RenderParameters.from(renderRequest))
  }

  @RequestMapping("/query", method = [RequestMethod.GET, RequestMethod.POST])
  fun query(
    @ModelAttribute("queryRequest") queryRequest: QueryRequest
  ): QueryResponse {
    val queryResponseData = QueryResponseData("scalar", mutableListOf(queryRequest.time, "2"))
    return QueryResponse("success", queryResponseData)
  }

  @RequestMapping("/query_range", method = [RequestMethod.GET])
  fun queryRange(
    @ModelAttribute("queryRangeRequest") queryRangeRequest: QueryRangeRequest
  ): QueryRangeResponse {
    return prometheusRenderHandler.handle(queryRangeRequest)
  }

  @RequestMapping("/label/__name__/values", method = [RequestMethod.GET])
  fun getNames(): PrometheusResponse {
    return PrometheusResponse("success", tagSearchHandler.getTagValues(
      "",
      emptyList(),
      "@name",
      0,
      60,
      Int.MAX_VALUE
    ))
  }
}
