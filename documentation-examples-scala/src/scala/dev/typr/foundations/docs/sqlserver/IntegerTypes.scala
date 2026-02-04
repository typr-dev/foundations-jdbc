package dev.typr.foundations.docs.sqlserver
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object IntegerTypes:
  //start
  val tinyType: SqlServerType[Uint1] = SqlServerTypes.tinyint   // Note: unsigned!
  val intType: SqlServerType[Int] = SqlServerTypes.int_
  val bigType: SqlServerType[Long] = SqlServerTypes.bigint
  //stop
