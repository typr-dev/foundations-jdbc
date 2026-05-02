package dev.typr.foundationssc.docs.sqlite
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object RealTypes:
  // start
  val realType: SqliteType[Double] = SqliteTypes.real
  val doubleType: SqliteType[Double] = SqliteTypes.double_
  val floatType: SqliteType[Float] = SqliteTypes.float_
  // stop
