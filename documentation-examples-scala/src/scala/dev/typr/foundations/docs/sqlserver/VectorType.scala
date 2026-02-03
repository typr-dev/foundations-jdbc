package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}

@SuppressWarnings(Array("unused"))
object VectorType:
  //start
  val vectorType: SqlServerType[Array[Byte]] = SqlServerTypes.vector
  //stop
