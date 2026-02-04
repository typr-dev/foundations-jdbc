package dev.typr.foundations.docs.sqlserver
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object RowversionType:
  //start
  val rowversionType: SqlServerType[Array[Byte]] = SqlServerTypes.rowversion
  //stop
