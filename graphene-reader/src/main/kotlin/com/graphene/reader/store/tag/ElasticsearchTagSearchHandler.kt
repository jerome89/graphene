package com.graphene.reader.store.tag

import com.graphene.reader.service.tag.TagSearchHandler
import com.graphene.reader.store.key.handler.ElasticsearchClient
import com.graphene.reader.store.tag.optimizer.ElasticsearchTagSearchQueryOptimizer
import com.graphene.reader.store.tag.optimizer.TagSearchTarget
import java.util.Objects
import java.util.stream.Collectors
import org.apache.commons.lang3.RegExUtils
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component

@Component
class ElasticsearchTagSearchHandler(
  private val elasticsearchClient: ElasticsearchClient,
  private val elasticsearchTagSearchQueryOptimizer: ElasticsearchTagSearchQueryOptimizer
) : TagSearchHandler {

  private val logger = LogManager.getLogger(javaClass)

  override fun getTags(tagPrefix: String, tagExpressions: MutableList<String>, tag: String, from: Long, to: Long, limit: Int): List<String> {
    return if (tagExpressions.isNullOrEmpty()) {
      val prefix = RegExUtils.removeAll(tagPrefix, "\\s+")
      val fieldToInspect = "$TAGS_BUCKET_NAME.$prefix*"
      getTagsFromMappings(fieldToInspect, from, to)
    } else {
      val tagsToExclude = tagExpressions.stream().map { t -> t.split(EQUALS)[0] }.collect(Collectors.toList())
      val tagSearchTarget = TagSearchTarget(tagExpressions = tagExpressions)
      getTagsFromSearchResults(tagSearchTarget, from, to, tagsToExclude)
    }
  }

  override fun getTagValues(valuePrefix: String?, tagExpressions: MutableList<String>, tag: String, from: Long, to: Long, limit: Int): List<String> {
    val tagValues = mutableSetOf<String>()
    try {
      val response = elasticsearchClient.query(
        elasticsearchTagSearchQueryOptimizer.optimize(TagSearchTarget(tagKey = tag, tagValue = valuePrefix, tagExpressions = tagExpressions)),
        from,
        to,
        "$TAGS_BUCKET_NAME.$tag",
        limit
      )
      tagValues.addAll(response.tagValues)
    } catch (e: Exception) {
      logger.warn("Failed to find Tag Values: {tag: $tag}, {valuePrefix: $valuePrefix}, {tagExpressions: $tagExpressions}")
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
        to,
        TAGS_BUCKET_NAME
      )
      val scrollIds = mutableListOf<String>()
      while (response.hits.hits.isNotEmpty()) {
        for (hit in response.hits) {
          if (Objects.nonNull(hit.sourceAsMap[TAGS_BUCKET_NAME])) {
            (hit.sourceAsMap[TAGS_BUCKET_NAME] as Map<String, *>).keys.forEach {
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

  private fun getTagsFromMappings(fieldToInspect: String, from: Long, to: Long): List<String> {
    val result = mutableSetOf<String>()
    try {
      val response = elasticsearchClient.getFieldMapping(fieldToInspect, from, to)
      for (mappings in response.fieldMappings) {
        for (key in mappings.keys) {
          val mappingPieces = key.split(DOT)
          if (mappingPieces.size > 1) {
            result.add(mappingPieces[1])
          }
        }
      }
    } catch (e: Exception) {
      logger.warn("Get Mappings request failed: " + e.message)
    }
    return result.sorted()
  }

  companion object {
    internal val TAGS_BUCKET_NAME = "tags"
    internal val DOT = "."
    internal val EQUALS = "="
  }
}
