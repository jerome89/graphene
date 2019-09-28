package com.graphene.writer.util

import java.util.concurrent.ThreadFactory

/**
 * @author Andrei Ivanov
 */
class NamedThreadFactory(private val baseName: String) : ThreadFactory {

  private var counter = 0

  override fun newThread(r: Runnable): Thread {
    return Thread(r, baseName + "-" + counter++)
  }
}
