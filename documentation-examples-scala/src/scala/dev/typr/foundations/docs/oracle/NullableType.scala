package dev.typr.foundations.docs.oracle
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object NullableType:
  //start
  val notNull: OracleType[Int] = OracleTypes.numberInt
  val nullable: OracleType[Option[Int]] = OracleTypes.numberInt.nullable
  //stop
