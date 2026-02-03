package dev.typr.foundations.docs.oracle

import dev.typr.foundations.{OracleType, OracleTypes, PaddedString}

@SuppressWarnings(Array("unused"))
object PaddedStringTypes:
  //start
  val padded: OracleType[PaddedString] = OracleTypes.charPadded(10)  // CHAR(10)
  val npadded: OracleType[PaddedString] = OracleTypes.ncharPadded(10) // NCHAR(10)
  //stop
