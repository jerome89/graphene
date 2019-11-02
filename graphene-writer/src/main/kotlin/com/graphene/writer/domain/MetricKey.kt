package com.graphene.writer.domain

/**
 * @author Andrei Ivanov
 */
class MetricKey(val tenant: String, val path: String, val rollup: Int, val period: Int, val timestamp: Long) {

  override fun equals(o: Any?): Boolean {
    if (this === o) return true
    if (o !is MetricKey) return false

    val metricKey = o as MetricKey?

    return period == metricKey!!.period && rollup == metricKey.rollup && timestamp == metricKey.timestamp && path == metricKey.path && tenant == metricKey.tenant
  }

  override fun hashCode(): Int {
    var result = tenant.hashCode()
    result = 31 * result + path.hashCode()
    result = 31 * result + rollup
    result = 31 * result + period
    result = 31 * result + (timestamp xor timestamp.ushr(32)).toInt()
    return result
  }

  override fun toString(): String {
    return "MetricKey{" +
      "tenant='" + tenant + '\''.toString() +
      ", path='" + path + '\''.toString() +
      ", rollup=" + rollup +
      ", period=" + period +
      ", timestamp=" + timestamp +
      '}'.toString()
  }
}
