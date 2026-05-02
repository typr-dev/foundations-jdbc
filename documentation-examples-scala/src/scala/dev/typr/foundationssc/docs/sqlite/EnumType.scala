package dev.typr.foundationssc.docs.sqlite
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object EnumType:
  // start
  // Plain Scala 3 enum
  enum Status:
    case PENDING, ACTIVE, COMPLETED

  // SQLite has no native enum — pair with a CHECK (col IN (...)) constraint in DDL.
  val statusType: SqliteType[Status] = SqliteTypes.ofEnum(Status.values)
  // stop
