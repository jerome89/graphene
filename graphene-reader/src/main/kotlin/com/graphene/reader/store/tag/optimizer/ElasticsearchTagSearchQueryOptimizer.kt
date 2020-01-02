package com.graphene.reader.store.tag.optimizer

import org.elasticsearch.index.query.QueryBuilder

interface ElasticsearchTagSearchQueryOptimizer {
  fun optimize(tagSearchTarget: TagSearchTarget): QueryBuilder
}
