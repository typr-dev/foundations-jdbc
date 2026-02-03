package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}
import dev.typr.foundations.data.{Uint1, Uint4}
import java.math.BigInteger

@SuppressWarnings(Array("unused"))
object IntegerTypesUnsigned:
  //start
  val utinyType: DuckDbType[Uint1] = DuckDbTypes.utinyint
  val uintType: DuckDbType[Uint4] = DuckDbTypes.uinteger
  val uhugeType: DuckDbType[BigInteger] = DuckDbTypes.uhugeint
  //stop
