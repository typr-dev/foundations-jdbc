package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


import java.math.BigInteger

@SuppressWarnings(Array("unused"))
object IntegerTypesUnsigned:
  //start
  val utinyType: DuckDbType[Uint1] = DuckDbTypes.utinyint
  val uintType: DuckDbType[Uint4] = DuckDbTypes.uinteger
  val uhugeType: DuckDbType[BigInteger] = DuckDbTypes.uhugeint
  //stop
