package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}
import java.math.BigInteger

@SuppressWarnings(Array("unused"))
object IntegerTypesSigned:
  //start
  val tinyType: DuckDbType[java.lang.Byte] = DuckDbTypes.tinyint
  val intType: DuckDbType[Integer] = DuckDbTypes.integer
  val hugeType: DuckDbType[BigInteger] = DuckDbTypes.hugeint
  //stop
