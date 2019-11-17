// package com.graphene.writer.input.influxdb
//
// import com.graphene.writer.input.MetricConverter
// import com.graphene.writer.input.GrapheneMetric
// import net.iponweb.disthene.reader.utils.MetricRule
// import java.util.*
//
// class InfluxDbMetricConverter : MetricConverter<InfluxDbController.InfluxDbMetric> {
//
//  override fun convert(metric: InfluxDbController.InfluxDbMetric): GrapheneMetric {
//    return GrapheneMetric(
//      Collections.emptyMap(),
//      convertTags(metric),
//      metric.value,
//      metric.timestampSeconds
//    )
//  }
//
//  private fun convertTags(metric: GraphiteMetric): MutableMap<String, String> {
//    val separatedMetricKey = metric.key.split(DELIMITER)
//
//    val tags = mutableMapOf<String, String>()
//    for ((index, key) in separatedMetricKey.withIndex()) {
//      tags["$index"] = MetricRule.generate(key)
//    }
//    return tags
//  }
//
//  companion object {
//    private const val DELIMITER = "."
//  }
// }
