package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object BitStringType:
  //start
  val bitType: DuckDbType[String] = DuckDbTypes.bit
  val bit8: DuckDbType[String] = DuckDbTypes.bit(8)  // BIT(8)
  //stop
