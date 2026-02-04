package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object BitStringType:
  //start
  val bitType: DuckDbType[String] = DuckDbTypes.bit
  val bit8: DuckDbType[String] = DuckDbTypes.bit(8)  // BIT(8)
  //stop
