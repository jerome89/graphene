package com.graphene.writer.input

import net.iponweb.disthene.reader.utils.MetricRule
import java.util.*
import java.util.function.IntConsumer
import java.util.stream.IntStream

/***
 *
 * @since 1.0.0
 * @author dark
 */
data class GrapheneMetric(
  val meta: Map<String, String>,
  val tags: Map<String, String>,
  val value: Double,
  val timestamp: Long
) {

  fun getGraphiteKey(): String {
    val stringJoiner = StringJoiner(DOT)

    IntStream.range(0, tags.size)
      .forEach(IntConsumer {
        stringJoiner.add(tags[it.toString()])
      })

    return stringJoiner.toString()
  }

  fun getId(): String {
    return "${meta.getOrDefault("tenant", MetricRule.defaultTenant())}_${getGraphiteKey()}"
  }

  fun getTenant(): String {
    return meta.getOrDefault("tenant", MetricRule.defaultTenant())
  }

  companion object {
    const val DOT = "."
  }
}
