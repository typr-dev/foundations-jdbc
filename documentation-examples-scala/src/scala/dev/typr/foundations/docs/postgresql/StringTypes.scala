package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}

@SuppressWarnings(Array("unused"))
object StringTypes:
  //start
  val textType: PgType[String] = PgTypes.text
  val charType: PgType[String] = PgTypes.bpchar(10) // char(10)
  //stop
