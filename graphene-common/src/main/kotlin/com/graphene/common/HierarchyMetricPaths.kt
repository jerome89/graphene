package com.graphene.common

import com.google.common.base.Preconditions

class HierarchyMetricPaths {

  companion object {

    fun of(path: String, leaf: Boolean): HierarchyMetricPath {
      Preconditions.checkNotNull(path, "path must be set!")
      Preconditions.checkNotNull(leaf, "leaf must be set!")

      val leafOrBranch = if (leaf) LEAF else BRANCH

      return HierarchyMetricPath(
        leafOrBranch,
        leafOrBranch,
        leafOrBranch,
        path,
        path.substring(path.lastIndexOf('.') + 1)
      )
    }

    const val LEAF = 0
    const val BRANCH = 1
  }

  data class HierarchyMetricPath internal constructor(
    var allowChildren: Int,
    var expandable: Int,
    var leaf: Int,
    var id: String,
    var text: String
  )
}
