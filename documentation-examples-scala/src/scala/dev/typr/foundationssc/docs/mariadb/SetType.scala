package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object SetType:
  //start
  val setType: MariaType[MariaSet] = MariaTypes.set

  // Create and use sets
  val values: MariaSet = MariaSet.of("read", "write")
  val csv: String = values.toCommaSeparated()
  //stop
