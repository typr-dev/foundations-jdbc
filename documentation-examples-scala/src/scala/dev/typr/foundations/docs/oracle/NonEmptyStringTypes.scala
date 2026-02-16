package dev.typr.foundations.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object NonEmptyStringTypes:
  //start
  val nonEmpty: OracleType[NonEmptyString] = OracleTypes.varchar2NonEmpty(100)
  val nvarNonEmpty: OracleType[NonEmptyString] = OracleTypes.nvarchar2NonEmpty(100)
  //stop
