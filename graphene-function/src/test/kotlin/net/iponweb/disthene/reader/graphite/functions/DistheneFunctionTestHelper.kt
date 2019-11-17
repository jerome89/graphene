package net.iponweb.disthene.reader.graphite.functions

import com.graphene.common.utils.DateTimeUtils
import io.mockk.every
import io.mockk.mockk
import net.iponweb.disthene.reader.beans.TimeSeries
import net.iponweb.disthene.reader.graphite.PathTarget
import net.iponweb.disthene.reader.graphite.evaluation.EvaluationContext
import net.iponweb.disthene.reader.graphite.evaluation.TargetEvaluator
import net.iponweb.disthene.reader.graphite.utils.ValueFormatter
import net.iponweb.disthene.reader.service.index.IndexService
import net.iponweb.disthene.reader.service.metric.MetricService

abstract class DistheneFunctionTestHelper {

  private val indexService = mockk<IndexService>()
  private val metricService = mockk<MetricService>()

  private val targetEvaluator = TargetEvaluator(metricService, indexService)
  private lateinit var pathTarget: PathTarget

  internal abstract fun `should evaluate time series data by function`()
  internal abstract fun `should check invalid arguments by function's rule`()
  internal abstract fun `should pass valid arguments by function's rule`()

  fun targetEvaluator(): TargetEvaluator {
    return targetEvaluator
  }

  internal fun setUpTimeSeriesList(distheneFunction: DistheneFunction, timeSeriesList: List<TimeSeries>) {
    setUpTimeSeriesKeys(timeSeriesList)
    setUpTimeSeriesData(timeSeriesList)

    setUpFunctionArguments(distheneFunction, timeSeriesList)
  }

  private fun setUpFunctionArguments(distheneFunction: DistheneFunction, timeSeriesList: List<TimeSeries>) {
    pathTarget = PathTarget(TIME_SERIES_NAME_1, EvaluationContext(ValueFormatter.getInstance(ValueFormatter.ValueFormatterType.MACHINE)), TIME_SERIES_NAME_1, "NONE", DateTimeUtils.from("2019-10-10 10:00:00"), DateTimeUtils.from("2019-10-10 10:02:00"))

    distheneFunction.addArg(pathTarget)
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
