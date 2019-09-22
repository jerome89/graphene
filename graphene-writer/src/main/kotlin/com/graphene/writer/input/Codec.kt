package com.graphene.writer.input

/***
 *
 * @since 1.0.0
 * @author dark
 */
interface Codec<T> {

  fun encode(metric: T): GrapheneMetric

}
