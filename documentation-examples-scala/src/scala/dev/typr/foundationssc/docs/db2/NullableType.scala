package dev.typr.foundationssc.docs.db2
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object NullableType:
  //start
  val notNull: Db2Type[Int] = Db2Types.integer
  val nullable: Db2Type[Option[Int]] = Db2Types.integer.opt
  //stop
