package com.graphene.reader.controller.graphene

import com.graphene.common.beans.Path
import com.graphene.reader.format.Format
import com.graphene.reader.format.ResponseFormatter.formatResponse
import com.graphene.reader.handler.RenderParameter
import com.graphene.reader.service.metric.DataFetchHandler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MetricsController(
  private var dataFetchHandler: DataFetchHandler
) {
  @GetMapping("/metrics")
  fun getMetrics(
    @RequestParam(required = false) tenant: String,
    @RequestParam path: List<Path>,
    @RequestParam from: Long,
    @RequestParam to: Long
  ): ResponseEntity<*> {
    val renderParameter = RenderParameter(targets = arrayListOf(), format = Format.JSON)
    val timeSeriesList = dataFetchHandler.getMetrics(tenant, path, from, to)
    return formatResponse(timeSeriesList, renderParameter)
  }

  @PostMapping("/metrics")
  fun postMetrics(@RequestBody parameters: MetricsParameters): ResponseEntity<*> {
    val renderParameter = RenderParameter(targets = arrayListOf(), format = Format.JSON)
    val timeSeriesList = dataFetchHandler.getMetrics(parameters.tenant!!, parameters.path, parameters.from, parameters.to)
    return formatResponse(timeSeriesList, renderParameter)
  }

  data class MetricsParameters(
    var tenant: String?,
    var path: List<Path>,
    var from: Long,
    var to: Long
  )
}
