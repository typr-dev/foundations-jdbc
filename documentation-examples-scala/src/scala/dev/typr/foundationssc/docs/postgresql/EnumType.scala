package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object EnumType:
  // start
  // Scala 3 enums need an explicit `extends java.lang.Enum[Self]` clause
  // to satisfy the `ofEnum[E <: Enum[E]]` bound. Without it, the type
  // checker rejects the call even though simple Scala 3 enums do extend
  // java.lang.Enum at the JVM level.
  enum Status extends java.lang.Enum[Status]:
    case PENDING, ACTIVE, COMPLETED

  val statusType: PgType[Status] =
    PgTypes.ofEnum[Status]("status", Status.valueOf)
  // stop
