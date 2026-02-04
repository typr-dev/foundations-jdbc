package dev.typr.foundations.docs.postgresql

import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


@SuppressWarnings(Array("unused"))
object BoolType:
  //start
  val boolType: PgType[Boolean] = PgTypes.bool
  //stop
