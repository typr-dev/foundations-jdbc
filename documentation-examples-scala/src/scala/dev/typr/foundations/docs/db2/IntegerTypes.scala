package dev.typr.foundations.docs.db2
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object IntegerTypes:
  //start
  val smallType: Db2Type[Short] = Db2Types.smallint
  val intType: Db2Type[Int] = Db2Types.integer
  val bigType: Db2Type[Long] = Db2Types.bigint
  //stop
