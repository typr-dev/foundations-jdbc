package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}
import java.util.UUID

@SuppressWarnings(Array("unused"))
object UuidType:
  //start
  val uuidType: PgType[UUID] = PgTypes.uuid
  //stop
