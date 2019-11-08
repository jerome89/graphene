package com.graphene.writer.input.influxdb

import com.graphene.writer.processor.GrapheneProcessor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

//@RestController
class InfluxDbController(
  private val grapheneProcessor: GrapheneProcessor
) {

//  lateinit var influxDbMetricConverter: InfluxDbMetricConverter
//
//  @PostConstruct
//  fun init() {
//    influxDbMetricConverter = InfluxDbMetricConverter()
//  }

//  @PostMapping("/write")
  fun write(
    @RequestParam(name = "db") db: String,
    @RequestParam(name = "consistency", required = false) consistency: String?,
    @RequestParam(name = "precision", required = false) precision: String?,
    @RequestParam(name = "u", required = false) user: String?,
    @RequestParam(name = "p", required = false) password: String?,
    @RequestParam(name = "rp", required = false, defaultValue = "DEFAULT") retentionPolicyName: String,
    @RequestBody influxDbMetric: InfluxDbMetric
  ): String {

    println(influxDbMetric)
//    grapheneProcessor.process(influxDbMetricConverter.convert(influxDbMetric))

    return "OK"
  }

  data class InfluxDbMetric(
    var metric: String
//    var measurement: String,
//    var tags: Map<String, String>,
//    var fields: Map<String, Double>,
//    var timestamp: Long
  )
}
