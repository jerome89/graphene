package com.graphene.writer.exception

class Exceptions {

  companion object {
    fun unsupportedKeyStoreHandlerException(message: String): UnsupportedKeyStoreHandlerException = UnsupportedKeyStoreHandlerException(message)
  }
}

class UnsupportedKeyStoreHandlerException(message: String) : Throwable(message)
