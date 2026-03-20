package dev.typr.foundationssc.docs.mariadb

import dev.typr.foundationssc.{MariaType, MariaTypes}
import java.util.UUID

//noinspection ScalaUnusedSymbol
class UuidType {
  //start
  val uuidType: MariaType[UUID] = MariaTypes.uuid
  //stop
}
