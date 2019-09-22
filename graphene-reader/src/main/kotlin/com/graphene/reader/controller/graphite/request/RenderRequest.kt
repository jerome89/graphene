package com.graphene.reader.controller.graphite.request

data class RenderRequest(
  val target: List<String>,
  val from: String?,
  val until: String?,
  val format: String?,
  val maxDataPoints: Int?
)