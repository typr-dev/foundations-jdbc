package dev.typr.foundationssc.docs.sqlserver
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object StringTypes:
  // start
  val charType: SqlServerType[String] = SqlServerTypes.char_Of(10)
  val varcharType: SqlServerType[String] = SqlServerTypes.varcharOf(255)
  val varcharMax: SqlServerType[String] = SqlServerTypes.varcharMax
  // stop
