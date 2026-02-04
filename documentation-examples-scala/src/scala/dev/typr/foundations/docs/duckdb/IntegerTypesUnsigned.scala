package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.math.BigInteger

@SuppressWarnings(Array("unused"))
object IntegerTypesUnsigned:
  //start
  val utinyType: DuckDbType[Uint1] = DuckDbTypes.utinyint
  val uintType: DuckDbType[Uint4] = DuckDbTypes.uinteger
  val uhugeType: DuckDbType[BigInteger] = DuckDbTypes.uhugeint
  //stop
