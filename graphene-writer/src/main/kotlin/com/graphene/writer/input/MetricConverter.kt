package com.graphene.writer.input

/***
 *
 * @since 1.0.0
 * @author dark
 */
interface MetricConverter<T> {

  @Throws(UnexpectedConverterException::class)
  fun convert(metric: T): GrapheneMetric
}
