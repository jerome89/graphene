package com.graphene.writer.store.key.handler

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.key.ElasticsearchClientFactory
import com.graphene.writer.store.key.GrapheneIndexRequest
import com.graphene.writer.store.key.KeyStoreHandlerProperty
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory
import java.util.TreeMap

class TagBasedKeyStoreHandler(
  private val elasticsearchClientFactory: ElasticsearchClientFactory,
  val property: KeyStoreHandlerProperty
) : AbstractElasticsearchKeyStoreHandler(elasticsearchClientFactory, property) {

  // TODO
  /**
   *
   * @meta : {
   *   "type" : InfluxDB,
   *   "measurement" : cpu
   * },
   *
   * @meta : {
   *   "type" : Graphite,
   *   "measurement" : none
   * }
   *
   *
   *
   */
  override fun mapToGrapheneIndexRequests(metric: GrapheneMetric?): List<GrapheneIndexRequest> {
    val grapheneIndexRequests = mutableListOf<GrapheneIndexRequest>()
    for (metricEntry in metric!!.metrics) {
      grapheneIndexRequests.add(GrapheneIndexRequest("${metricEntry.key};${metric.getId()}", source(metric.tags, metricEntry), metric.timestampMillis()))
    }

    return grapheneIndexRequests
  }

  override fun templateSource(): String = SOURCE

  override fun templateName(): String = TEMPLATE_NAME

  private fun source(tags: TreeMap<String, String>, metricEntry: MutableMap.MutableEntry<String, Double>): XContentBuilder {
    val source = XContentFactory.jsonBuilder()
      .startObject()

    val tags = tags.entries
    for (tag in tags) {
      source.field(tag.key, tag.value)
    }

    source.field("metric", metricEntry.key)

    return source.endObject()
  }


  companion object {
    const val TEMPLATE_NAME = "tag-based-key-path-template"
    const val SOURCE = """
      {
        "settings": {
          "number_of_replicas": 0,
          "number_of_shards": 5,
          "auto_expand_replicas": "0-1"
        },
        "mappings": {
          "path": {
            "dynamic_templates": [
              {
                "else": {
                  "match": "*",
                  "unmatch": "depth",
                  "match_mapping_type": "string",
                  "mapping": {
                    "type": "keyword"
                  }
                }
              }
            ]
          }
        }
      }
    """
  }

}
