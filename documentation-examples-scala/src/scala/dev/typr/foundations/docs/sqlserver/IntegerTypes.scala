package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}
import dev.typr.foundations.data.Uint1

@SuppressWarnings(Array("unused"))
object IntegerTypes:
  //start
  val tinyType: SqlServerType[Uint1] = SqlServerTypes.tinyint   // Note: unsigned!
  val intType: SqlServerType[Integer] = SqlServerTypes.int_
  val bigType: SqlServerType[java.lang.Long] = SqlServerTypes.bigint
  //stop
