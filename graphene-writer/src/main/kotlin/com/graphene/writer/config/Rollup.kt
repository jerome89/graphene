package com.graphene.writer.config

/**
 * @author Andrei Ivanov
 */
class Rollup(s: String) {

  var rollup: Int = 0
  var period: Int = 0

  init {
    val ss = s.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    rollup = Integer.parseInt(ss[0].substring(0, ss[0].length - 1))
    period = (java.lang.Long.parseLong(ss[1].substring(0, ss[1].length - 1)) / rollup).toInt()
  }

  override fun toString(): String {
    return "Rollup{" +
      "rollup=" + rollup +
      ", period=" + period +
      '}'.toString()
  }
}
