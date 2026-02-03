package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{DbType, PgType, PgTypes}
import dev.typr.foundations.scala.DbTypeOps

@SuppressWarnings(Array("unused"))
object NullableTypes:
  //start
  val notNull: PgType[Integer] = PgTypes.int4
  val nullable: DbType[Option[Integer]] = PgTypes.int4.nullable  // null values allowed
  //stop
