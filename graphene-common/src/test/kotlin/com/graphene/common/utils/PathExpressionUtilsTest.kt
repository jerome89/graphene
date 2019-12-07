package com.graphene.common.utils

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

internal class PathExpressionUtilsTest {

  @Test
  fun `should return encoded metric path`() {
    assertEquals(PathExpressionUtils.getEscapedPathExpression("hosts.service.role"), "hosts\\.service\\.role")
    assertEquals(PathExpressionUtils.getEscapedPathExpression("hosts.service.role.*"), "hosts\\.service\\.role\\.[^\\.]*")
  }

  @Test
  internal fun `should compare with the path expression`() {
    assertEquals(PathExpressionUtils.isPlainPath("hosts.service.role"), true)

    assertEquals(PathExpressionUtils.isPlainPath("hosts.service.role.*"), false)
    assertEquals(PathExpressionUtils.isPlainPath("hosts.service.role.{a,b}"), false)
  }
}
