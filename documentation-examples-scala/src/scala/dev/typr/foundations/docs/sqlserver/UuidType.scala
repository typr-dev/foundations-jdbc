package dev.typr.foundations.docs.sqlserver
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.util.UUID

@SuppressWarnings(Array("unused"))
object UuidType:
  //start
  val uuidType: SqlServerType[UUID] = SqlServerTypes.uniqueidentifier
  //stop
