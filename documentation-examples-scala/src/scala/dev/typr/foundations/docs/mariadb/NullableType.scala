package dev.typr.foundations.docs.mariadb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object NullableType:
  //start
  val notNull: MariaType[Int] = MariaTypes.int_
  val nullable: MariaType[Option[Int]] = MariaTypes.int_.opt
  //stop
