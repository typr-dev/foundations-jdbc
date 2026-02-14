package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object NullableTypes:
  //start
  val notNull: DuckDbType[Int] = DuckDbTypes.integer
  val nullable: DuckDbType[Option[Int]] = DuckDbTypes.integer.opt
  //stop
