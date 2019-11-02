package net.iponweb.disthene.reader.utils

import org.junit.Test

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

class WildcardUtilTest {

  @Test
  fun shouldReturnEncodedMetricPath() {
    assertThat(WildcardUtil.getPathsRegExFromWildcard("hosts.service.role"), `is`("hosts\\.service\\.role"))
    assertThat(WildcardUtil.getPathsRegExFromWildcard("hosts.service.role.*"), `is`("hosts\\.service\\.role\\.[^\\.]*"))
  }
}
