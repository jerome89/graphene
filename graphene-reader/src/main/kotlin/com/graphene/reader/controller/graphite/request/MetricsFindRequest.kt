package com.graphene.reader.controller.graphite.request

import net.iponweb.disthene.reader.utils.DateTimeUtils

data class MetricsFindRequest(
  val query: String,
  val from: Long = DateTimeUtils.currentTimeSeconds() - 3_600,
  val until: Long = DateTimeUtils.currentTimeSeconds()
)
