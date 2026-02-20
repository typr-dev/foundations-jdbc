package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object NullableTypes:
  //start
  val notNull: DuckDbType[Int] = DuckDbTypes.integer
  val nullable: DuckDbType[Option[Int]] = DuckDbTypes.integer.opt
  //stop
