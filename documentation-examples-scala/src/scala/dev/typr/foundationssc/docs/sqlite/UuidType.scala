package dev.typr.foundationssc.docs.sqlite
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object UuidType:
  // start
  val uuidType: SqliteType[java.util.UUID] = SqliteTypes.uuid
  // stop
