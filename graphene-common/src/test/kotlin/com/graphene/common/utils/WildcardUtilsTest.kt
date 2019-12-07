package com.graphene.common.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class WildcardUtilsTest {

  @Test
  fun `should return encoded metric path`() {
    assertEquals(WildcardUtils.getPathsRegExFromWildcard("hosts.service.role"), "hosts\\.service\\.role")
    assertEquals(WildcardUtils.getPathsRegExFromWildcard("hosts.service.role.*"), "hosts\\.service\\.role\\.[^\\.]*")
  }

  @Test
  internal fun `should compare with the path expression`() {
    assertEquals(WildcardUtils.isPlainPath("hosts.service.role"), true)

    assertEquals(WildcardUtils.isPlainPath("hosts.service.role.*"), false)
    assertEquals(WildcardUtils.isPlainPath("hosts.service.role.{a,b}"), false)
  }
}
