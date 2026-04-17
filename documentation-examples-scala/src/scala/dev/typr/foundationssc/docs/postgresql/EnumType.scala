package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object EnumType:
  // start
  // Plain Scala 3 enum — no extends java.lang.Enum needed
  enum Status:
    case PENDING, ACTIVE, COMPLETED

  // Create a PgType — just pass .values
  val statusType: PgType[Status] =
    PgTypes.ofEnum("status", Status.values)
  // stop
