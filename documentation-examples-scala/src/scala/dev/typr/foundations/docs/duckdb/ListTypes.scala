package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.math.BigDecimal
import java.time.LocalDate
import java.util.{List, UUID}

@SuppressWarnings(Array("unused"))
object ListTypes:
  //start
  // Pre-defined list types with optimized native JNI support
  val listInt: DuckDbType[List[Integer]] = DuckDbTypes.listInteger
  val listStr: DuckDbType[List[String]] = DuckDbTypes.listVarchar
  val listDouble: DuckDbType[List[java.lang.Double]] = DuckDbTypes.listDouble

  // Types that use SQL literal conversion (slightly slower but correct)
  val listUuid: DuckDbType[List[UUID]] = DuckDbTypes.listUuid
  val listDate: DuckDbType[List[LocalDate]] = DuckDbTypes.listDate
  val listDecimal: DuckDbType[List[BigDecimal]] = DuckDbTypes.listDecimal
  //stop
