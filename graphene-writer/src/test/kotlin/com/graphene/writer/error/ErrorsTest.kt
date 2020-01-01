package com.graphene.writer.error

import com.graphene.writer.error.exception.UnknownGrapheneWriterException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ErrorsTest {

  @Test
  internal fun `should match each exception type's code and exception`() {
    assertEquals(-1, Errors.UNKNOWN_EXCEPTION.code)
    assertEquals(UnknownGrapheneWriterException::class.java, Errors.UNKNOWN_EXCEPTION.exception().javaClass)
  }
}
