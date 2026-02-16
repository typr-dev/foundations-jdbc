package dev.typr.foundations.docs.postgresql

import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object BoolType:
  //start
  val boolType: PgType[Boolean] = PgTypes.bool
  //stop
