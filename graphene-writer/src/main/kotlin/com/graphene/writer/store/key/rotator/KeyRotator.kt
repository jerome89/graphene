package com.graphene.writer.store.key.rotator

interface KeyRotator : Runnable {

  fun getCurrentPointer(): String

}
