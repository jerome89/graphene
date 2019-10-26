package com.graphene.writer.store.key.model

abstract class ElasticsearchClientDecorator(
  val elasticsearchClient: ElasticsearchClient
) : ElasticsearchClient
