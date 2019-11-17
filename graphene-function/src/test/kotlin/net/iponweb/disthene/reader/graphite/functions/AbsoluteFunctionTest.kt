package net.iponweb.disthene.reader.graphite.functions

import com.graphene.common.utils.DateTimeUtils
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import net.iponweb.disthene.reader.exceptions.InvalidArgumentException
import net.iponweb.disthene.reader.graphite.PathTarget
import net.iponweb.disthene.reader.graphite.evaluation.EvaluationContext
import net.iponweb.disthene.reader.graphite.utils.ValueFormatter
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AbsoluteFunctionTest : DistheneFunctionTestHelper() {

  lateinit var absoluteFunction: AbsoluteFunction

  @BeforeEach
  internal fun setUp() {
    absoluteFunction = AbsoluteFunction(FUNCTION_NAME)
  }

  @Test
  override fun `should evaluate time series data by function`() {
    // given
    setUpTimeSeriesList(
      absoluteFunction,
      listOf(
        timeSeries(TIME_SERIES_NAME_1, "2019-10-10 10:00:00", "2019-10-10 10:02:00", 60, arrayOf(-0.0, -0.1)),
        timeSeries(TIME_SERIES_NAME_2, "2019-10-10 10:00:00", "2019-10-10 10:02:00", 60, arrayOf(-2.0, -1.0))
      )
    )

    // when
    val timeSeriesList = absoluteFunction.evaluate(targetEvaluator())

    // then
    assertThat(arrayOf(0.0, 0.1), `is`(timeSeriesList[0].values))
    assertThat(arrayOf(2.0, 1.0), `is`(timeSeriesList[1].values))
  }

  @Test
  override fun `should check invalid arguments by function's rule`() {
    // given
    val table = table(
      headers("argument"),
      row(NUMERIC_ARGUMENT),
      row(null)
    )

    // when
    table.forAll { argument ->
      absoluteFunction.addArg(argument)

      assertThrows<InvalidArgumentException> { absoluteFunction.checkArguments() }
    }
  }

  @Test
  override fun `should pass valid arguments by function's rule`() {
    // given
    val pathTarget = PathTarget(
      TIME_SERIES_NAME_1,
      EvaluationContext(ValueFormatter.getInstance(ValueFormatter.ValueFormatterType.MACHINE)),
      TIME_SERIES_NAME_1,
      "NONE",
      DateTimeUtils.from("2019-10-10 10:00:00"),
      DateTimeUtils.from("2019-10-10 10:02:00")
    )

    absoluteFunction.addArg(pathTarget)

    // when
    assertDoesNotThrow(absoluteFunction::checkArguments)
  }

  companion object {
    const val FUNCTION_NAME = "absolute"
    const val NUMERIC_ARGUMENT = 99.9
  }
}
