package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}

@SuppressWarnings(Array("unused"))
object BoolType:
  //start
  val boolType: PgType[java.lang.Boolean] = PgTypes.bool
  //stop
