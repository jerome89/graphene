package com.graphene.common.beans

import com.google.common.collect.Maps

class Path(var path: String) {
  private var tags: MutableMap<String, String> = Maps.newHashMap()

  fun setTags(tags: MutableMap<String, String>) {
    this.tags = tags
  }

  fun getTags(): Map<String, String> {
    return tags
  }

  fun addTag(tagKey: String, tagValue: String) {
    tags[tagKey] = tagValue
  }

  override fun equals(obj: Any?): Boolean {
    if (obj !is Path) {
      return false
    }
    return path == obj.path
  }

  override fun hashCode(): Int {
    return path.hashCode()
  }
}
