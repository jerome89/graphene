package com.graphene.writer.store

import com.graphene.writer.input.GrapheneMetric
import java.io.Closeable

/**
 *
 * @author dark
 * @since 1.0.0
 */
interface StoreHandler : Closeable {

  fun handle(grapheneMetric: GrapheneMetric)

}

interface KeyStoreHandler : StoreHandler

interface DataStoreHandler : StoreHandler
