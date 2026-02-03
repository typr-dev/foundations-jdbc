package dev.typr.foundations.docs.oracle

import dev.typr.foundations.{NonEmptyString, OracleType, OracleTypes}

@SuppressWarnings(Array("unused"))
object NonEmptyStringTypes:
  //start
  val nonEmpty: OracleType[NonEmptyString] = OracleTypes.varchar2NonEmpty(100)
  val nvarNonEmpty: OracleType[NonEmptyString] = OracleTypes.nvarchar2NonEmpty(100)
  //stop
