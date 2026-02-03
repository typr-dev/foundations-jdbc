package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}

@SuppressWarnings(Array("unused"))
object BinaryTypes:
  //start
  val binaryType: SqlServerType[Array[Byte]] = SqlServerTypes.binary(16)
  val varbinaryType: SqlServerType[Array[Byte]] = SqlServerTypes.varbinary(255)
  val varbinaryMax: SqlServerType[Array[Byte]] = SqlServerTypes.varbinaryMax
  //stop
