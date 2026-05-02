package dev.typr.foundationssc.docs.sqlite
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object IntegerTypes:
  // start
  val integerType: SqliteType[Long] = SqliteTypes.integer
  val bigintType: SqliteType[Long] = SqliteTypes.bigint
  val intType: SqliteType[Int] = SqliteTypes.int_
  val smallintType: SqliteType[Short] = SqliteTypes.smallint
  val tinyintType: SqliteType[Byte] = SqliteTypes.tinyint
  // stop
