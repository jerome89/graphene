package com.graphene.writer.config

/**
 * @author Andrei Ivanov
 */
class IndexBulkConfiguration {

  var actions: Int = 10000
  var interval: Long = 500

  override fun toString(): String {
    return "IndexBulkConfiguration{" +
      "actions=" + actions +
      ", interval=" + interval +
      '}'.toString()
  }
}
