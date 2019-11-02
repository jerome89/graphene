package com.graphene.reader.controller.graphite.request

import com.graphene.common.utils.DateTimeUtils

data class MetricsFindRequest(
  val query: String,
  val from: Long = DateTimeUtils.currentTimeSeconds() - 3_600,
  val until: Long = DateTimeUtils.currentTimeSeconds()
)
