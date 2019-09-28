package com.graphene.writer.domain

import com.graphene.writer.config.Rollup
import net.iponweb.disthene.reader.utils.MetricRule

/**
 * @author Andrei Ivanov
 */
class Metric {

  private var key: MetricKey? = null
  var value: Double = 0.toDouble()

  val id: String
    get() = tenant + "_" + path

  val tenant: String
    get() = key!!.tenant

  val path: String
    get() = key!!.path

  val rollup: Int
    get() = key!!.rollup

  val period: Int
    get() = key!!.period

  val timestamp: Long
    get() = key!!.timestamp

  constructor(input: String, rollup: Rollup) {
    val splitInput = input.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    // We were interning tenant and path here - we are going to store them all (or almost so) constantly anyhow in multiple places
    // In fact this also work for a moderate metrics stream. Once we start receiving 10s of millions different metrics, it tends to degrade quite a bit
    // So, leaving this only for tenant
    this.key = MetricKey(
      if (splitInput.size >= 4) splitInput[3].intern() else MetricRule.defaultTenant(),
      splitInput[0],
      rollup.rollup,
      rollup.period,
      normalizeTimestamp(java.lang.Long.parseLong(splitInput[2]), rollup))
    this.value = java.lang.Double.parseDouble(splitInput[1])
  }

  constructor(tenant: String, path: String, rollup: Int, period: Int, value: Double, timestamp: Long) {
    this.key = MetricKey(tenant, path, rollup, period, timestamp)
    this.value = value
  }

  constructor(key: MetricKey, value: Double) {
    this.key = key
    this.value = value
  }

  private fun normalizeTimestamp(timestamp: Long, rollup: Rollup): Long {
    return timestamp / rollup.rollup * rollup.rollup
  }

  override fun toString(): String {
    return "Metric{" +
      "key=" + key +
      ", value=" + value +
      '}'.toString()
  }
}
