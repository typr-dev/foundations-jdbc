package dev.typr.foundationssc.docs.sqlserver
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


import java.util.UUID

@SuppressWarnings(Array("unused"))
object UuidType:
  //start
  val uuidType: SqlServerType[UUID] = SqlServerTypes.uniqueidentifier
  //stop
