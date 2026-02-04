package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.util.UUID

@SuppressWarnings(Array("unused"))
object UuidType:
  //start
  val uuidType: DuckDbType[UUID] = DuckDbTypes.uuid
  //stop
