package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.{MariaType, MariaTypes}

@SuppressWarnings(Array("unused"))
object BooleanType:
  //start
  val boolType: MariaType[java.lang.Boolean] = MariaTypes.bool
  val bitBool: MariaType[java.lang.Boolean] = MariaTypes.bit1
  //stop
