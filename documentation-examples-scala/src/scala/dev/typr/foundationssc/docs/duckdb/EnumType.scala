package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object EnumType:
  // start
  // Define your Scala 3 enum
  enum Status:
    case PENDING, ACTIVE, COMPLETED

  // Create a DuckDbType using transform (Scala 3 enums don't extend java.lang.Enum)
  val statusType: DuckDbType[Status] = DuckDbTypes.varchar.transform(
    Status.valueOf,
    _.toString
  )
  // stop
