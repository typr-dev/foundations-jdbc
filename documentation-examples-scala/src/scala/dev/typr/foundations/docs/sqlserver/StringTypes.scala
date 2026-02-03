package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}

@SuppressWarnings(Array("unused"))
object StringTypes:
  //start
  val charType: SqlServerType[String] = SqlServerTypes.char_(10)
  val varcharType: SqlServerType[String] = SqlServerTypes.varchar(255)
  val varcharMax: SqlServerType[String] = SqlServerTypes.varcharMax
  //stop
