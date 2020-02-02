package com.graphene.reader.store.key.property

import com.graphene.reader.store.key.selector.KeySelectorProperty

/**
 * @author Andrei Ivanov
 * @author Dark
 *
 * @since 1.0.0
 */
interface IndexProperty {
  fun keySelectorProperty(): KeySelectorProperty
  fun clusterName(): String?
  fun index(): String?
  fun tenant(): String
  fun type(): String?
  fun cluster(): List<String>
  fun port(): Int
  fun scroll(): Int
  fun timeout(): Int
  fun maxPaths(): Int
}
