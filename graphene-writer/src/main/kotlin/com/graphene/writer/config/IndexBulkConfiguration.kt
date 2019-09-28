package com.graphene.writer.config

/**
 * @author Andrei Ivanov
 */
class IndexBulkConfiguration {

  var actions: Int = 0
  var interval: Int = 0

  override fun toString(): String {
    return "IndexBulkConfiguration{" +
      "actions=" + actions +
      ", interval=" + interval +
      '}'.toString()
  }
}
