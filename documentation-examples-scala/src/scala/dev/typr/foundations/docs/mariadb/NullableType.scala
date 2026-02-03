package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.{MariaType, MariaTypes}
import java.util.Optional

@SuppressWarnings(Array("unused"))
object NullableType:
  //start
  val notNull: MariaType[Integer] = MariaTypes.int_
  val nullable: MariaType[Optional[Integer]] = MariaTypes.int_.opt()
  //stop
