package dev.typr.foundations.docs.mariadb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object SetType:
  //start
  val setType: MariaType[MariaSet] = MariaTypes.set

  // Create and use sets
  val values: MariaSet = MariaSet.of("read", "write")
  val csv: String = values.toCommaSeparated()
  //stop
