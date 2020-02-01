package com.graphene.reader.error.exception

open class GrapheneReaderException(message: String) : Throwable(message)

class UnknownGrapheneReaderException(message: String) : GrapheneReaderException(message)

class UnsupportedKeySearchHandlerException(message: String) : GrapheneReaderException(message)

class UnsupportedDataFetchHandlerException(message: String) : GrapheneReaderException(message)

class IllegalArgumentException(message: String) : GrapheneReaderException(message)
