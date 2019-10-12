package com.graphene.writer.input.graphite

import java.util.*

interface GraphiteAware {

  fun getTags() : Map<String, String>

  fun getGraphiteKey(): String {
    val graphiteKey = StringJoiner(GraphiteMetric.DOT)

    IntRange(0, getTags().size - 1)
      .map { it.toString() }
      .map { getTags()[it] }
      .forEach { graphiteKey.add(it) }

    return graphiteKey.toString()
  }

  fun getGraphiteKeyParts(): List<String> {
    return getGraphiteKey().split("\\.".toRegex())
  }
}
