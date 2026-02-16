package dev.typr.foundations.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object PaddedStringTypes:
  //start
  val padded: OracleType[PaddedString] = OracleTypes.charPadded(10)  // CHAR(10)
  val npadded: OracleType[PaddedString] = OracleTypes.ncharPadded(10) // NCHAR(10)
  //stop
