package dev.typr.foundationssc.docs.sqlserver
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object VectorType:
  // start
  val vectorType: SqlServerType[Array[Byte]] = SqlServerTypes.vector
  // stop
