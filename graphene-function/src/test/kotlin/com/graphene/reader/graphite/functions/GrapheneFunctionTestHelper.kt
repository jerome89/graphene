package com.graphene.reader.graphite.functions

import com.graphene.common.utils.DateTimeUtils
import com.graphene.reader.beans.TimeSeries
import com.graphene.reader.graphite.PathTarget
import com.graphene.reader.graphite.evaluation.EvaluationContext
import com.graphene.reader.graphite.evaluation.TargetEvaluator
import com.graphene.reader.graphite.utils.ValueFormatter
import com.graphene.reader.service.index.IndexService
import com.graphene.reader.service.metric.MetricService
import io.mockk.every
import io.mockk.mockk

abstract class GrapheneFunctionTestHelper {

  private val indexService = mockk<IndexService>()
  private val metricService = mockk<MetricService>()

  private val targetEvaluator = TargetEvaluator(metricService, indexService)
  private lateinit var pathTarget: PathTarget

  internal abstract fun `should evaluate time series data by function`()
  internal abstract fun `should throw an exception if invalid arguments by function's rule`()
  internal abstract fun `shouldn't throw an exception if an argument is valid arguments by function's rule`()

  fun targetEvaluator(): TargetEvaluator {
    return targetEvaluator
  }

  internal fun setUpTimeSeriesList(grapheneFunction: GrapheneFunction, timeSeriesList: List<TimeSeries>) {
    setUpTimeSeriesKeys(timeSeriesList)
    setUpTimeSeriesData(timeSeriesList)

    setUpFunctionArguments(grapheneFunction, timeSeriesList)
  }

  private fun setUpFunctionArguments(grapheneFunction: GrapheneFunction, timeSeriesList: List<TimeSeries>) {
    pathTarget = PathTarget(TIME_SERIES_NAME_1, EvaluationContext(ValueFormatter.getInstance(ValueFormatter.ValueFormatterType.MACHINE)), TIME_SERIES_NAME_1, "NONE", DateTimeUtils.from("2019-10-10 10:00:00"), DateTimeUtils.from("2019-10-10 10:02:00"))

    grapheneFunction.addArg(pathTarget)
  }

  private fun setUpTimeSeriesData(timeSeriesList: List<TimeSeries>) {
    for (timeSeries in timeSeriesList) {
      every {
        metricService.getMetricsAsList(any(), any(), any(), any())
      } answers {
        timeSeriesList
      }
    }
  }

  private fun setUpTimeSeriesKeys(timeSeriesList: List<TimeSeries>) {
    val timeSeriesKeys = mutableSetOf<String>()
    for (timeSeries in timeSeriesList) {
      timeSeriesKeys.add(timeSeries.name)
    }

    every {
      indexService.getPaths(any(), any(), any(), any())
    } answers {
      timeSeriesKeys
    }
  }

  companion object {
    const val TIME_SERIES_NAME_1 = "hosts.server1.cpu.usage"
    const val TIME_SERIES_NAME_2 = "hosts.server2.cpu.usage"

    fun timeSeries(name: String, from: String, to: String, step: Int, values: Array<Double>): TimeSeries {
      return TimeSeries(name, DateTimeUtils.from(from), DateTimeUtils.from(to), step, values)
    }
  }
}
