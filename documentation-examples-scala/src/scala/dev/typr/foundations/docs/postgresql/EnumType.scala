package dev.typr.foundations.docs.postgresql
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object EnumType:
  //start
  // Define your Scala 3 enum
  enum Status:
    case PENDING, ACTIVE, COMPLETED

  // Create a PgType using bimap (Scala 3 enums don't extend java.lang.Enum)
  val statusType: PgType[Status] = PgTypes.text.bimap(
    Status.valueOf,
    _.toString
  )
  //stop
