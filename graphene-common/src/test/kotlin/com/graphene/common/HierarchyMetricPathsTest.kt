package com.graphene.common

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class HierarchyMetricPathsTest {

  @Test
  internal fun `should return HierarchyMetricPath`() {
    // when
    val hierarchyMetricPath = HierarchyMetricPaths.of("a.b.c", 2, false)

    // then
    assertEquals(1, hierarchyMetricPath.allowChildren)
    assertEquals(1, hierarchyMetricPath.expandable)
    assertEquals(1, hierarchyMetricPath.leaf)
    assertEquals("a.b.c", hierarchyMetricPath.id)
    assertEquals("c", hierarchyMetricPath.text)
  }
}
