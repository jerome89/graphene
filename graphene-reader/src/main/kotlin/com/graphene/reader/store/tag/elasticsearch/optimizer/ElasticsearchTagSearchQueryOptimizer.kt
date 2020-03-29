package com.graphene.reader.store.tag.elasticsearch.optimizer

import org.elasticsearch.index.query.QueryBuilder

interface ElasticsearchTagSearchQueryOptimizer {
  fun optimize(tagSearchTarget: TagSearchTarget): QueryBuilder
}
