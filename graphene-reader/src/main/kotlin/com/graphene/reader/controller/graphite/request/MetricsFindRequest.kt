package com.graphene.reader.controller.graphite.request

import com.graphene.common.utils.DateTimeUtils

data class MetricsFindRequest(
  val query: String,
  private val from: Long = DateTimeUtils.currentTimeSeconds() - 3_600,
  private val until: Long = DateTimeUtils.currentTimeSeconds()
) {

  fun fromMillis(): Long {
    return from * 1_000
  }

  fun untilMillis(): Long {
    return until * 1_000
  }
}
