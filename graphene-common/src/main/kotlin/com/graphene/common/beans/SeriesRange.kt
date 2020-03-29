package com.graphene.common.beans

class SeriesRange(from: Long, to: Long, val rollup: Int) {
  val from: Long = from / rollup * rollup
  val to: Long = to / rollup * rollup
  val expectedCount: Int
  override fun toString(): String {
    return "Effective from: $from, Effective to: $to, Expected Point Count: $expectedCount"
  }

  init {
    this.expectedCount = (this.to - this.from).toInt() / rollup + 1
  }
}
