package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}
import java.util.UUID

@SuppressWarnings(Array("unused"))
object ArrayTypes:
  //start
  val intArray: DuckDbType[Array[Integer]] = DuckDbTypes.integerArray
  val strArray: DuckDbType[Array[String]] = DuckDbTypes.varcharArray
  val uuidArray: DuckDbType[Array[UUID]] = DuckDbTypes.uuidArray

  // Create array for any type
  // val customArray: DuckDbType[Array[MyType]] = myType.array()
  //stop
