package com.graphene.common.beans

class QueryRange(from: Long, to: Long, rollup: Int) {
  val from: Long
  val to: Long
  val rollup: Int
  val expectedCount: Int
  override fun toString(): String {
    return "Effective from: $from, Effective to: $to, Expected Point Count: $expectedCount"
  }

  init {
    this.from = from / rollup * rollup
    this.to = to / rollup * rollup
    this.rollup = rollup
    this.expectedCount = (this.to - this.from).toInt() / rollup + 1
  }
}
