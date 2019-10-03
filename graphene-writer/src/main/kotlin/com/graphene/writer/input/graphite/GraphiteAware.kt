package com.graphene.writer.input.graphite

import java.util.*
import java.util.stream.IntStream

interface GraphiteAware {

  fun getTags() : Map<String, String>

  fun getGraphiteKey(): String {
    val graphiteKey = StringJoiner(GraphiteMetric.DOT)

    IntStream.range(0, getTags().size)
      .forEach {
        graphiteKey.add(getTags()[it.toString()])
      }

    return graphiteKey.toString()
  }

  fun getGraphiteKeyParts(): List<String> {
    return getGraphiteKey().split("\\.".toRegex())
  }
}
