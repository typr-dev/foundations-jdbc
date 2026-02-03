package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}

@SuppressWarnings(Array("unused"))
object BoolType:
  //start
  val bitType: SqlServerType[java.lang.Boolean] = SqlServerTypes.bit
  //stop
