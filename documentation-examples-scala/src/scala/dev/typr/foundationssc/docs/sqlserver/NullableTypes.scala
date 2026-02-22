package dev.typr.foundationssc.docs.sqlserver
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object NullableTypes:
  //start
  val notNull: SqlServerType[Int] = SqlServerTypes.int_
  val nullable: SqlServerType[Option[Int]] = SqlServerTypes.int_.opt
  //stop
