package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object StringTypes:
  //start
  val varcharType: DuckDbType[String] = DuckDbTypes.varchar
  val charType: DuckDbType[String] = DuckDbTypes.char_(10)
  //stop
