package dev.typr.foundations.docs.oracle

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class PaddedStringTypes {
    //start
    val padded: OracleType<PaddedString> = OracleTypes.charPadded(10)  // CHAR(10)
    val npadded: OracleType<PaddedString> = OracleTypes.ncharPadded(10) // NCHAR(10)
    //stop
}
