package dev.typr.foundations.docs.db2

import dev.typr.foundations.{Db2Type, Db2Types}

@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  //start
  val realType: Db2Type[java.lang.Float] = Db2Types.real
  val doubleType: Db2Type[java.lang.Double] = Db2Types.double_
  //stop
