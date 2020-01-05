package com.graphene.writer.input.graphite

import java.util.StringJoiner
import java.util.TreeMap

interface GraphiteAware {

  fun getGraphiteKey(tags: TreeMap<String, String>): String {
    val graphiteKey = StringJoiner(GraphiteMetric.DOT)

    IntRange(0, tags.size - 1)
      .map { it.toString() }
      .map { tags[it] }
      .forEach { graphiteKey.add(it) }

    return graphiteKey.toString()
  }
}
