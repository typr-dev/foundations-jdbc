package dev.typr.foundationssc.docs.sqlserver
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object UnicodeStringTypes:
  // start
  val ncharType: SqlServerType[String] = SqlServerTypes.ncharOf(10)
  val nvarcharType: SqlServerType[String] = SqlServerTypes.nvarcharOf(255)
  val nvarcharMax: SqlServerType[String] = SqlServerTypes.nvarcharMax
  // stop
