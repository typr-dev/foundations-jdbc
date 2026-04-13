package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@SuppressWarnings(Array("unused"))
object ListTypes:
  // start
  // Any type can be made into a list with .list()
  val listInt = DuckDbTypes.integer.list
  val listStr = DuckDbTypes.varchar.list
  val listDouble = DuckDbTypes.double_.list
  val listUuid = DuckDbTypes.uuid.list
  val listDate = DuckDbTypes.date.list
  val listDecimal = DuckDbTypes.decimal.list
  // stop
