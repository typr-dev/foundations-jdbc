package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.{MariaType, MariaTypes}

@SuppressWarnings(Array("unused"))
object IntegerTypesSigned:
  //start
  val tinyType: MariaType[java.lang.Byte] = MariaTypes.tinyint
  val intType: MariaType[Integer] = MariaTypes.int_
  val bigType: MariaType[java.lang.Long] = MariaTypes.bigint
  //stop
