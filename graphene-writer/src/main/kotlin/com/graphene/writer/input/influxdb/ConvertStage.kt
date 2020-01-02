package com.graphene.writer.input.influxdb

enum class ConvertStage {
  MEASUREMENT,

  TAG_KEY,

  TAG_VALUE,

  FIELD_KEY,

  FIELD_VALUE,

  TIMESTAMP
}
