package com.graphene.writer.store.key.model

import com.graphene.writer.config.IndexBulkConfiguration

open class ElasticsearchKeyStoreHandlerProperty(
  open var clusterName: String,
  open var index: String,
  open var type: String,
  open var cluster: List<String>,
  open var port: Int,
  open var bulk: IndexBulkConfiguration?
)
