package com.graphene.reader.error

import com.graphene.reader.error.exception.GrapheneReaderException
import com.graphene.reader.error.exception.IllegalArgumentException
import com.graphene.reader.error.exception.UnknownGrapheneReaderException
import com.graphene.reader.error.exception.UnsupportedDataFetchHandlerException
import com.graphene.reader.error.exception.UnsupportedKeySearchHandlerException
import java.util.function.Function

enum class Errors(
  val code: Int,
  val defaultMessage: String,
  private val builder: Function<String, GrapheneReaderException>
) {

  UNKNOWN_EXCEPTION(
    code = -1,
    defaultMessage = "Fail to handle logic because of encounter unknown error",
    builder = Function<String, GrapheneReaderException> { message: String -> UnknownGrapheneReaderException(message) }
  ),

  UNSUPPORTED_KEY_SEARCH_HANDLER_EXCEPTION(
    code = 0,
    defaultMessage = "Fail to initialize key store handler because it's not supported handler type",
    builder = Function<String, GrapheneReaderException> { message: String -> UnsupportedKeySearchHandlerException(message) }
  ),

  UNSUPPORTED_DATA_FETCH_HANDLER_EXCEPTION(
    code = 0,
    defaultMessage = "Fail to initialize data store handler because it's not supported handler type",
    builder = Function<String, GrapheneReaderException> { message: String -> UnsupportedDataFetchHandlerException(message) }
  ),

  ILLEGAL_ARGUMENT_EXCEPTION(
    code = 0,
    defaultMessage = "Fail to handle argument because it's illegal",
    builder = Function<String, GrapheneReaderException> { message: String -> IllegalArgumentException(message) }
  );

  fun exception(message: String = defaultMessage): GrapheneReaderException = builder.apply(message)
}
