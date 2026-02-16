package dev.typr.foundations.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object NullableType:
  //start
  val notNull: OracleType[Int] = OracleTypes.numberInt
  val nullable: OracleType[Option[Int]] = OracleTypes.numberInt.opt
  //stop
