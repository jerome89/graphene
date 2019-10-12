package com.graphene.writer.input

/***
 *
 * @since 1.0.0
 * @author dark
 */
interface MetricConverter<T> {

  fun convert(metric: T): GrapheneMetric

}
