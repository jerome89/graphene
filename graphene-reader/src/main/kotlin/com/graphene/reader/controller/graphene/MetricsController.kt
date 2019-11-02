package com.graphene.reader.controller.graphene

import net.iponweb.disthene.reader.service.metric.CassandraMetricService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MetricsController(
  private var cassandraMetricService: CassandraMetricService
) {

  @GetMapping("/metrics")
  fun getMetrics(
    @RequestParam(required = false) tenant: String,
    @RequestParam path: List<String>,
    @RequestParam from: Long,
    @RequestParam to: Long
  ): String? {
    return cassandraMetricService.getMetricsAsJson(tenant, path, from, to)
  }

  @PostMapping("/metrics")
  fun postMetrics(@RequestBody parameters: MetricsParameters): String? {
    return cassandraMetricService.getMetricsAsJson(parameters.tenant, parameters.path, parameters.from, parameters.to)
  }

  data class MetricsParameters(
    var tenant: String?,
    var path: List<String>,
    var from: Long,
    var to: Long
  )
}
