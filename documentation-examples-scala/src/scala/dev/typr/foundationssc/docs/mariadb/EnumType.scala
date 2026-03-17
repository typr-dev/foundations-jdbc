package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object EnumType:
  // start
  // Define your Scala 3 enum
  enum Status:
    case PENDING, ACTIVE, COMPLETED

  // Create a MariaType using transform (Scala 3 enums don't extend java.lang.Enum)
  val statusType: MariaType[Status] = MariaTypes.text.transform(
    Status.valueOf,
    _.toString
  )
  // stop
