package com.graphene.reader.utils

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class WildcardUtilTest {

  @Test
  fun shouldReturnEncodedMetricPath() {
    assertThat(WildcardUtil.getPathsRegExFromWildcard("hosts.service.role"), `is`("hosts\\.service\\.role"))
    assertThat(WildcardUtil.getPathsRegExFromWildcard("hosts.service.role.*"), `is`("hosts\\.service\\.role\\.[^\\.]*"))
  }
}
