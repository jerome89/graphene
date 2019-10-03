package com.graphene.writer.input.graphite

import java.util.*
import java.util.stream.IntStream

interface GraphiteAware {

  fun getTags() : Map<String, String>

  fun getGraphiteKey(): String {
    val stringJoiner = StringJoiner(GraphiteMetric.DOT)

    IntStream.range(0, getTags().size)
      .forEach {
        stringJoiner.add(getTags()[it.toString()])
      }

    return stringJoiner.toString()
  }

  fun getHierarchyGraphiteKey(): List<String> {
    return getGraphiteKey().split("\\.".toRegex())
  }
}
