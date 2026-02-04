package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.math.BigInteger

@SuppressWarnings(Array("unused"))
object IntegerTypesSigned:
  //start
  val tinyType: DuckDbType[Byte] = DuckDbTypes.tinyint
  val intType: DuckDbType[Int] = DuckDbTypes.integer
  val hugeType: DuckDbType[java.math.BigInteger] = DuckDbTypes.hugeint
  //stop
