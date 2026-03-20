package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.util.UUID

@SuppressWarnings(Array("unused"))
object ArrayTypes:
  // start
  val intArray: DuckDbType[Array[Int]] = DuckDbTypes.integerArray
  val strArray: DuckDbType[Array[String]] = DuckDbTypes.varcharArray
  val uuidArray: DuckDbType[Array[UUID]] = DuckDbTypes.uuidArray

  // Create array for any type
  // val customArray: DuckDbType[Array[MyType]] = myType.array()
  // stop
