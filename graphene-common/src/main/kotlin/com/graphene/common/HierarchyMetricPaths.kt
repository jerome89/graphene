package com.graphene.common

import com.google.common.base.Preconditions

class HierarchyMetricPaths {

  companion object {

    fun of(path: String, depth: Int, leaf: Boolean): HierarchyMetricPath {
      Preconditions.checkNotNull(path, "path must be set!")
      Preconditions.checkNotNull(depth, "depth must be set!")
      Preconditions.checkNotNull(leaf, "leaf must be set!")

      return HierarchyMetricPath(
        if (leaf) 0 else 1,
        if (leaf) 0 else 1,
        if (leaf) 0 else 1,
        path,
        path.substring(path.lastIndexOf('.') + 1)
      )
    }
  }

  data class HierarchyMetricPath internal constructor(
    var allowChildren: Int = 0,
    var expandable: Int = 0,
    var leaf: Int = 0,
    var id: String,
    var text: String
  )
}
