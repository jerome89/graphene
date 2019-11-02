package com.graphene.common

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

internal class HierarchyMetricPathsTest {

  @Test
  internal fun `should return leaf hierarchy metric path`() {
    // when
    val hierarchyMetricPath = HierarchyMetricPaths.of("a.b.c", true)

    // then
    assertEquals(HierarchyMetricPaths.LEAF, hierarchyMetricPath.allowChildren)
    assertEquals(HierarchyMetricPaths.LEAF, hierarchyMetricPath.expandable)
    assertEquals(HierarchyMetricPaths.LEAF, hierarchyMetricPath.leaf)
    assertEquals("a.b.c", hierarchyMetricPath.id)
    assertEquals("c", hierarchyMetricPath.text)
  }

  @Test
  internal fun `should return branch hierarchy metric path`() {
    // when
    val hierarchyMetricPath = HierarchyMetricPaths.of("a.b", false)

    // then
    assertEquals(HierarchyMetricPaths.BRANCH, hierarchyMetricPath.allowChildren)
    assertEquals(HierarchyMetricPaths.BRANCH, hierarchyMetricPath.expandable)
    assertEquals(HierarchyMetricPaths.BRANCH, hierarchyMetricPath.leaf)
    assertEquals("a.b", hierarchyMetricPath.id)
    assertEquals("b", hierarchyMetricPath.text)
  }
}
