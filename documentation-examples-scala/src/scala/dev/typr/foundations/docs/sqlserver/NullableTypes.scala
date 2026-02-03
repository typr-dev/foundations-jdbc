package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}
import java.util.Optional

@SuppressWarnings(Array("unused"))
object NullableTypes:
  //start
  val notNull: SqlServerType[Integer] = SqlServerTypes.int_
  val nullable: SqlServerType[Optional[Integer]] = SqlServerTypes.int_.opt()
  //stop
