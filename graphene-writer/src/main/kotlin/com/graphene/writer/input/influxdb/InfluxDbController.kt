package com.graphene.writer.input.influxdb

import com.graphene.writer.input.UnexpectedConverterException
import com.graphene.writer.processor.GrapheneProcessor
import javax.annotation.PostConstruct
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class InfluxDbController(
  private val grapheneProcessor: GrapheneProcessor
) {

  private val log = LogManager.getLogger(javaClass)

  lateinit var influxDbMetricConverter: InfluxDbMetricConverter

  @PostConstruct
  fun init() {
    influxDbMetricConverter = InfluxDbMetricConverter()
  }

  @PostMapping("/write", consumes = ["text/plain;charset=utf-8"])
  fun write(
    @RequestParam(name = "db") db: String,
    @RequestParam(name = "consistency", required = false) consistency: String?,
    @RequestParam(name = "precision", required = false) precision: String?,
    @RequestParam(name = "u", required = false) user: String?,
    @RequestParam(name = "p", required = false) password: String?,
    @RequestParam(name = "rp", required = false, defaultValue = "DEFAULT") retentionPolicyName: String,
    @RequestBody metrics: String
  ): ResponseEntity<*> {

    val metricsByLine = metrics.split("\n")
    for (metric in metricsByLine) {
      if (metric.isEmpty()) {
        continue
      }

      try {
        val grapheneMetrics = influxDbMetricConverter.convert(metric)
        for (grapheneMetric in grapheneMetrics) {
          grapheneProcessor.process(grapheneMetric)
        }
      } catch (e: UnexpectedConverterException) {
        log.warn("Fail to write metric : $metric")
      }
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(EMPTY)
  }

  companion object {
    const val EMPTY = ""
  }
}
