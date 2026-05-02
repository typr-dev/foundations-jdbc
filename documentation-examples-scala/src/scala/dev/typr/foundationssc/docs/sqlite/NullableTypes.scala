package dev.typr.foundationssc.docs.sqlite
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object NullableTypes:
  // start
  val notNull: SqliteType[Long] = SqliteTypes.integer
  val nullable: SqliteType[Option[Long]] = SqliteTypes.integer.opt
  // stop
