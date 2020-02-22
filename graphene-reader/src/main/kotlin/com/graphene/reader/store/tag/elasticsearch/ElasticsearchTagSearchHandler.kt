package com.graphene.reader.store.tag.elasticsearch

import com.graphene.reader.service.tag.TagSearchHandler
import com.graphene.reader.store.key.elasticsearch.handler.ElasticsearchClient
import com.graphene.reader.store.tag.elasticsearch.optimizer.ElasticsearchTagSearchQueryOptimizer
import com.graphene.reader.store.tag.elasticsearch.optimizer.TagSearchTarget
import java.util.Objects
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component

/**
 *
 * @author jerome89
 * @since 1.6.0
 */
@Component
class ElasticsearchTagSearchHandler(
  private val elasticsearchClient: ElasticsearchClient,
  private val elasticsearchTagSearchQueryOptimizer: ElasticsearchTagSearchQueryOptimizer
) : TagSearchHandler {

  private val logger = LogManager.getLogger(javaClass)

  override fun getTags(tagPrefix: String, tagExpressions: MutableList<String>, tag: String, from: Long, to: Long): List<String> {
    return if (tagExpressions.isNullOrEmpty()) {
      return listOf(NAME_FIELD)
    } else {
      val tagsToExclude = mutableListOf<String>()
      for (tagExpression in tagExpressions) {
        val head = tagExpression.split(EQUALS)[0]
        if (head.endsWith('!')) {
          tagsToExclude.add(head.substring(0, head.length - 1))
        } else {
          tagsToExclude.add(head)
        }
      }
      if (! tagsToExclude.contains(NAME_FIELD)) {
        return listOf(NAME_FIELD)
      } else if (tagsToExclude.size == 1) {
        return getTagsFromMappings(from, to)
      } else {
        val tagSearchTarget = TagSearchTarget(tagExpressions = tagExpressions)
        getTagsFromSearchResults(tagSearchTarget, from, to, tagsToExclude)
      }
    }
  }

  override fun getTagValues(valuePrefix: String?, tagExpressions: MutableList<String>, tag: String, from: Long, to: Long, limit: Int): List<String> {
    val tagValues = mutableSetOf<String>()
    if (tagExpressions.isEmpty() && tag == NAME_FIELD) {
      val scrollIds = mutableListOf<String>()
      try {
        var response = elasticsearchClient.queryNames()
        if (StringUtils.isNotBlank(response.scrollId)) {
          while (response.hits.hits.isNotEmpty()) {
            for (hit in response.hits) {
              tagValues.add(hit.id)
            }
            response = elasticsearchClient.searchScroll(response)
            scrollIds.add(response.scrollId)
          }
          if (scrollIds.isNotEmpty()) {
            elasticsearchClient.clearScroll(scrollIds)
          }
        } else {
          for (hit in response.hits) {
            tagValues.add(hit.id)
          }
        }
      } catch (e: Exception) {
        logger.warn("Failed to find metric names!")
      }
    } else {
      try {
        val response = elasticsearchClient.query(
          elasticsearchTagSearchQueryOptimizer.optimize(TagSearchTarget(tagKey = tag, tagValue = valuePrefix, tagExpressions = tagExpressions)),
          from,
          to,
          tag,
          limit
        )
        tagValues.addAll(response.tagValues)
      } catch (e: Exception) {
        logger.warn("Failed to find Tag Values: {tag: $tag}, {valuePrefix: $valuePrefix}, {tagExpressions: $tagExpressions}")
      }
    }
    return tagValues.sorted()
  }

  private fun getTagsFromSearchResults(
    tagSearchTarget: TagSearchTarget,
    from: Long,
    to: Long,
    tagsToExclude: List<String>
  ): List<String> {
    val result = mutableSetOf<String>()
    try {
      var response = elasticsearchClient.query(
        elasticsearchTagSearchQueryOptimizer.optimize(tagSearchTarget),
        from,
        to
      )
      val scrollIds = mutableListOf<String>()
      while (response.hits.hits.isNotEmpty()) {
        for (hit in response.hits) {
          if (Objects.nonNull(hit.sourceAsMap)) {
            (hit.sourceAsMap as Map<String, *>).keys.forEach {
              if (!tagsToExclude.contains(it)) {
                result.add(it)
              }
            }
          }
        }
        response = elasticsearchClient.searchScroll(response)
        scrollIds.add(response.scrollId)
      }

      if (scrollIds.isNotEmpty()) {
        elasticsearchClient.clearScroll(scrollIds)
      }
    } catch (e: Exception) {
      logger.warn("Failed to find tags search results: {")
    }
    return result.sorted()
  }

  private fun getTagsFromMappings(from: Long, to: Long): List<String> {
    val result = mutableSetOf<String>()
    try {
      val response = elasticsearchClient.getFieldMapping(from, to)
      for (mappingMeta in response.mappings) {
        if (Objects.nonNull(mappingMeta.sourceAsMap[MAPPING_PROPERTIES])) {
          (mappingMeta.sourceAsMap[MAPPING_PROPERTIES] as Map<String, *>).keys.forEach {
            if (!StringUtils.equals(it, NAME_FIELD)) {
              result.add(it)
            }
          }
        }
      }
    } catch (e: Exception) {
      logger.warn("Get Mappings request failed: " + e.message)
    }
    return result.sorted()
  }

  companion object {
    internal val MAPPING_PROPERTIES = "properties"
    internal val NAME_FIELD = "@name"
    internal val EQUALS = "="
  }
}
