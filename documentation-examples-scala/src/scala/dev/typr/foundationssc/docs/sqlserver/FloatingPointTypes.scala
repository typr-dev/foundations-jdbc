package dev.typr.foundationssc.docs.sqlserver
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  // start
  val realType: SqlServerType[Float] = SqlServerTypes.real
  val floatType: SqlServerType[Double] = SqlServerTypes.float_
  // stop
