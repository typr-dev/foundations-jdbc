package dev.typr.foundations.docs.oracle
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object RowIdTypes:
  //start
  val rowidType: OracleType[String] = OracleTypes.rowId
  val urowidType: OracleType[String] = OracleTypes.uRowId
  val urowid1000: OracleType[String] = OracleTypes.uRowId(1000)
  //stop
