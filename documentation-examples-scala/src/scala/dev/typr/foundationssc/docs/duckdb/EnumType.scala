package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object EnumType:
  // start
  // Plain Scala 3 enum — no extends java.lang.Enum needed
  enum Status:
    case PENDING, ACTIVE, COMPLETED

  // Create DuckDbType — just pass .values
  val statusType: DuckDbType[Status] =
    DuckDbTypes.ofEnum("status", Status.values)
  // stop
