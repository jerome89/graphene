package com.graphene.writer.error.exception

open class GrapheneWriterException(message: String) : Throwable(message)

class UnknownGrapheneWriterException(message: String) : GrapheneWriterException(message)

class UnsupportedKeyStoreHandlerException(message: String) : GrapheneWriterException(message)

class UnsupportedDataStoreHandlerException(message: String) : GrapheneWriterException(message)

class IllegalArgumentException(message: String) : GrapheneWriterException(message)
