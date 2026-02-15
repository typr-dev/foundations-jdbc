package dev.typr.foundations.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object NullableType:
  //start
  val notNull: MariaType[Int] = MariaTypes.int_
  val nullable: MariaType[Option[Int]] = MariaTypes.int_.opt
  //stop
