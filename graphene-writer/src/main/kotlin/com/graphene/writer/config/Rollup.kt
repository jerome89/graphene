package com.graphene.writer.config

/**
 * @author Andrei Ivanov
 */
class Rollup(s: String) {

  var rollup: Int = 0
  var retention: Int = 0

  init {
    val ss = s.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    rollup = Integer.parseInt(ss[0].substring(0, ss[0].length - 1))
    retention = Integer.parseInt(ss[1].substring(0, ss[1].length - 1))
  }

  override fun toString(): String {
    return "Rollup{" +
      "rollup=" + rollup +
      ", retention=" + retention +
      '}'.toString()
  }
}
