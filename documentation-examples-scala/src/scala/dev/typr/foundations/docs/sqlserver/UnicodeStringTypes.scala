package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}

@SuppressWarnings(Array("unused"))
object UnicodeStringTypes:
  //start
  val ncharType: SqlServerType[String] = SqlServerTypes.nchar(10)
  val nvarcharType: SqlServerType[String] = SqlServerTypes.nvarchar(255)
  val nvarcharMax: SqlServerType[String] = SqlServerTypes.nvarcharMax
  //stop
