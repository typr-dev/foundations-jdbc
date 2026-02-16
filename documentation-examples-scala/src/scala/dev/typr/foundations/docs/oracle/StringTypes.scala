package dev.typr.foundations.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object StringTypes:
  //start
  val varcharType: OracleType[String] = OracleTypes.varchar2
  val varchar100: OracleType[String] = OracleTypes.varchar2(100)
  val charType: OracleType[String] = OracleTypes.char_(10)
  val nvarcharType: OracleType[String] = OracleTypes.nvarchar2(100)
  //stop
