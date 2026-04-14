package dev.typr.foundationssc.docs.mariadb

import dev.typr.foundationssc.{MariaType, MariaTypes}
import dev.typr.foundations.data.Vector

//noinspection ScalaUnusedSymbol
class VectorType {
  // start
  val embedding: MariaType[Vector] = MariaTypes.vector(1536)
  // stop
}
