package com.graphene.writer.error

import com.graphene.writer.error.exception.GrapheneWriterException
import com.graphene.writer.error.exception.UnknownGrapheneWriterException
import com.graphene.writer.error.exception.UnsupportedKeyStoreHandlerException
import com.graphene.writer.error.exception.IllegalArgumentException
import java.util.function.Function

enum class Errors(
  val code: Int,
  val defaultMessage: String,
  private val builder: Function<String, GrapheneWriterException>
) {

  UNKNOWN_EXCEPTION(
    code = -1,
    defaultMessage = "Fail to handle logic because of encounter unknown error",
    builder = Function<String, GrapheneWriterException> { message: String -> UnknownGrapheneWriterException(message) }
  ),

  UNSUPPORTED_KEY_STORE_HANDLER_EXCEPTION(
    code = 0,
    defaultMessage = "Fail to initialize key store handler because it's not supported handler type",
    builder = Function<String, GrapheneWriterException> { message: String -> UnsupportedKeyStoreHandlerException(message) }
  ),

  ILLEGAL_ARGUMENT_EXCEPTION(
    code = 0,
    defaultMessage = "Fail to handle argument because it's illegal",
    builder = Function<String, GrapheneWriterException> { message: String -> IllegalArgumentException(message) }
  );

  fun exception(message: String = defaultMessage): GrapheneWriterException = builder.apply(message)
}
