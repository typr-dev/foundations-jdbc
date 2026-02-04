package dev.typr.foundations.docs.sqlserver
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object NullableTypes:
  //start
  val notNull: SqlServerType[Int] = SqlServerTypes.int_
  val nullable: SqlServerType[Option[Int]] = SqlServerTypes.int_.nullable
  //stop
