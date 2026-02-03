package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}

@SuppressWarnings(Array("unused"))
object EnumType:
  //start
  // Define your Scala 3 enum
  enum Status:
    case PENDING, ACTIVE, COMPLETED

  // Create a DuckDbType using bimap (Scala 3 enums don't extend java.lang.Enum)
  val statusType: DuckDbType[Status] = DuckDbTypes.varchar.bimap(
    Status.valueOf,
    _.toString
  )
  //stop
