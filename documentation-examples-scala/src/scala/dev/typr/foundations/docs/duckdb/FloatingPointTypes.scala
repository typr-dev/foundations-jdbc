package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  //start
  val floatType: DuckDbType[Float] = DuckDbTypes.float_
  val doubleType: DuckDbType[Double] = DuckDbTypes.double_
  //stop
