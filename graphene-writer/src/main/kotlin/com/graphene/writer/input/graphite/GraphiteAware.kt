package com.graphene.writer.input.graphite

import java.util.StringJoiner

interface GraphiteAware {

  fun getTags(): Map<String, String>

  fun getGraphiteKey(): String {
    val graphiteKey = StringJoiner(",")

    for (tag in getTags()) {
      graphiteKey.add("${tag.key}=${tag.value}")
    }

    return graphiteKey.toString()
  }

  fun getGraphiteKeyParts(): List<String> {
    return getGraphiteKey().split("\\.".toRegex())
  }
}
