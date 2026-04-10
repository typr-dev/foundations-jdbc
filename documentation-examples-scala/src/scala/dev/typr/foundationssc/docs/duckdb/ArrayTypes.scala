package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.util.UUID

@SuppressWarnings(Array("unused"))
object ArrayTypes:
  // start
  // Any type can be made into an array with .array()
  val intArray: DuckDbType[Array[Int]] = DuckDbTypes.integer.array()
  val strArray: DuckDbType[Array[String]] = DuckDbTypes.varchar.array()
  val uuidArray: DuckDbType[Array[UUID]] = DuckDbTypes.uuid.array()
  // stop
