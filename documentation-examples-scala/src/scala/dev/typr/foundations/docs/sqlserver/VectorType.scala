package dev.typr.foundations.docs.sqlserver
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object VectorType:
  //start
  val vectorType: SqlServerType[Array[Byte]] = SqlServerTypes.vector
  //stop
