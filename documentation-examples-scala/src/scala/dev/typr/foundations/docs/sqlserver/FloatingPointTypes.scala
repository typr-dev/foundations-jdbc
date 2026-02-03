package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}

@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  //start
  val realType: SqlServerType[java.lang.Float] = SqlServerTypes.real
  val floatType: SqlServerType[java.lang.Double] = SqlServerTypes.float_
  //stop
