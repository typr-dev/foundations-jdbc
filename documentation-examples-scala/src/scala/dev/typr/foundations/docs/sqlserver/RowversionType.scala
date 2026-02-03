package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}

@SuppressWarnings(Array("unused"))
object RowversionType:
  //start
  val rowversionType: SqlServerType[Array[Byte]] = SqlServerTypes.rowversion
  //stop
