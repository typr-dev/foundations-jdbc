package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object EnumType:
  // start
  // Plain Scala 3 enum — no extends java.lang.Enum needed
  enum Status:
    case PENDING, ACTIVE, COMPLETED

  // Create MariaType — derives ENUM('PENDING','ACTIVE','COMPLETED') from .values
  val statusType: MariaType[Status] =
    MariaTypes.ofEnum(Status.values)
  // stop
