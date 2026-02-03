package dev.typr.foundations.docs.oracle

import dev.typr.foundations.NonEmptyBlob
import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes

@Suppress("unused")
class BinaryTypes {
    //start
    val rawType: OracleType<ByteArray> = OracleTypes.raw
    val raw100: OracleType<ByteArray> = OracleTypes.raw(100)  // RAW(100)

    // Non-empty variant
    val rawNonEmpty: OracleType<NonEmptyBlob> = OracleTypes.rawNonEmpty(100)
    //stop
}
