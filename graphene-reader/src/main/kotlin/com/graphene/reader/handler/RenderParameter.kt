package com.graphene.reader.handler

import com.graphene.reader.controller.graphite.request.RenderRequest
import com.graphene.reader.exceptions.InvalidParameterValueException
import com.graphene.reader.format.Format
import com.graphene.reader.utils.DateUtils
import java.util.Objects
import org.apache.logging.log4j.LogManager
import org.joda.time.DateTimeZone

data class RenderParameter(
  var tenant: String? = null,
  val targets: List<String>,
  var tz: DateTimeZone? = null,
  var from: Long? = null,
  var until: Long? = null,
  var format: Format? = null,
  var maxDataPoints: Int = Integer.MAX_VALUE
)

object RenderParameters {
  internal val logger = LogManager.getLogger(RenderParameter::class.java)

  fun from(renderRequest: RenderRequest): RenderParameter {

    val timeZone: DateTimeZone = DateTimeZone.UTC

    // parse from defaulting to -1d
    var from: Long = getFrom(renderRequest.from, timeZone)
    val until: Long = getUntil(renderRequest.until, timeZone)

    // Prohibiting "from greater than until"
    if (from > until) {
      from = until
    }

    return RenderParameter(
      getTenant(),
      renderRequest.target,
      timeZone,
      from,
      until,
      Format.JSON,
      getMaxDataPoints(renderRequest.maxDataPoints)
    )
  }

  private fun getMaxDataPoints(maxDataPoints: Int?): Int {
    if (Objects.nonNull(maxDataPoints)) {
      try {
        return Integer.valueOf(maxDataPoints!!)
      } catch (e: NumberFormatException) {
        throw InvalidParameterValueException("maxDataPoints : $maxDataPoints")
      }
    }

    return Integer.MAX_VALUE
  }

  private fun getUntil(until: String?, timeZone: DateTimeZone): Long {
    if (Objects.nonNull(until)) {
      try {
        return DateUtils.parseExtendedTime(until, timeZone)
      } catch (e: NumberFormatException) {
        throw InvalidParameterValueException("DateTime format not recognized (until): $until")
      }
    }

    // default to now
    return System.currentTimeMillis() / 1000L
  }

  private fun getFrom(from: String?, timeZone: DateTimeZone): Long {
    if (Objects.nonNull(from)) {
      try {
        return DateUtils.parseExtendedTime(from.toString(), timeZone)
      } catch (e: NumberFormatException) {
        throw InvalidParameterValueException("DateTime format not recognized (from): $from")
      }
    }

    // default to -1d
    return System.currentTimeMillis() / 1000L - 86400
  }

  private fun getTenant(): String? = "NONE"
}
