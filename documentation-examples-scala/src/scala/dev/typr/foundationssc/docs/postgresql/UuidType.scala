package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.util.UUID

@SuppressWarnings(Array("unused"))
object UuidType:
  // start
  val uuidType: PgType[UUID] = PgTypes.uuid
  // stop
