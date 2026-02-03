package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.{MariaType, MariaTypes}
import dev.typr.foundations.data.maria.MariaSet

@SuppressWarnings(Array("unused"))
object SetType:
  //start
  val setType: MariaType[MariaSet] = MariaTypes.set

  // Create and use sets
  val values: MariaSet = MariaSet.of("read", "write")
  val csv: String = values.toCommaSeparated()
  //stop
