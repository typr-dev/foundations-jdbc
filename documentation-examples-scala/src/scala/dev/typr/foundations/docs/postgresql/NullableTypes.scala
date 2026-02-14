package dev.typr.foundations.docs.postgresql
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object NullableTypes:
  //start
  val notNull: PgType[Int] = PgTypes.int4
  val nullable: PgType[Option[Int]] = PgTypes.int4.opt
  //stop
