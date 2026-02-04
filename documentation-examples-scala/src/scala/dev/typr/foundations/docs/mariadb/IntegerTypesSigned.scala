package dev.typr.foundations.docs.mariadb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object IntegerTypesSigned:
  //start
  val tinyType: MariaType[Byte] = MariaTypes.tinyint
  val intType: MariaType[Int] = MariaTypes.int_
  val bigType: MariaType[Long] = MariaTypes.bigint
  //stop
