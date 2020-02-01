package com.graphene.reader.controller.graphene

import com.graphene.common.beans.Path
import com.graphene.reader.service.metric.DataFetchHandler
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
  ): String? {
    return dataFetchHandler.getMetricsAsJson(tenant, path, from, to)
  }

  @PostMapping("/metrics")
  fun postMetrics(@RequestBody parameters: MetricsParameters): String? {
    return dataFetchHandler.getMetricsAsJson(parameters.tenant!!, parameters.path, parameters.from, parameters.to)
  }

  data class MetricsParameters(
    var tenant: String?,
    var path: List<Path>,
    var from: Long,
    var to: Long
  )
}
