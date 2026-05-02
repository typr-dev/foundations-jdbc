package dev.typr.foundationssc.docs.sqlite
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object StringTypes:
  // start
  val textType: SqliteType[String] = SqliteTypes.text
  val varcharType: SqliteType[String] = SqliteTypes.varcharOf(255)
  val charType: SqliteType[String] = SqliteTypes.charOf(10)
  val clobType: SqliteType[String] = SqliteTypes.clob
  // stop
