package dev.typr.foundations.docs.sqlserver
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  //start
  val realType: SqlServerType[Float] = SqlServerTypes.real
  val floatType: SqlServerType[Double] = SqlServerTypes.float_
  //stop
