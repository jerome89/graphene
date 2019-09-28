package com.graphene.writer.store

import com.graphene.writer.input.GrapheneMetric

interface StoreHandler {

  fun handle(grapheneMetric: GrapheneMetric)

}
