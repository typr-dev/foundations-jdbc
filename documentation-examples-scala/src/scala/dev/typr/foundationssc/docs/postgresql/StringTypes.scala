package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object StringTypes:
  // start
  val textType: PgType[String] = PgTypes.text
  val charType: PgType[String] = PgTypes.bpchar(10) // char(10)
  // stop
