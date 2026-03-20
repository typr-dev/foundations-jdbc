package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object EnumType:
  // start
  // Define your Scala 3 enum
  enum Status:
    case PENDING, ACTIVE, COMPLETED

  // Create a PgType using transform (Scala 3 enums don't extend java.lang.Enum)
  val statusType: PgType[Status] = PgTypes.text.transform(
    Status.valueOf,
    _.toString
  )
  // stop
