package dev.typr.foundations.docs.db2

import dev.typr.foundations.{Db2Type, Db2Types}

@SuppressWarnings(Array("unused"))
object IntegerTypes:
  //start
  val smallType: Db2Type[java.lang.Short] = Db2Types.smallint
  val intType: Db2Type[Integer] = Db2Types.integer
  val bigType: Db2Type[java.lang.Long] = Db2Types.bigint
  //stop
