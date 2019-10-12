package com.graphene.writer.store

import com.graphene.writer.input.GrapheneMetric

/**
 *
 * @author dark
 * @since 1.0.0
 */
interface StoreHandler {

  fun handle(grapheneMetric: GrapheneMetric)

}

interface KeyStoreHandler : StoreHandler

interface DataStoreHandler : StoreHandler
