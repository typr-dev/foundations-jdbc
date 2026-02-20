package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class BinaryTypes {
    //start
    val rawType: OracleType<ByteArray> = OracleTypes.raw
    val raw100: OracleType<ByteArray> = OracleTypes.raw(100)  // RAW(100)

    // Non-empty variant
    val rawNonEmpty: OracleType<NonEmptyBlob> = OracleTypes.rawNonEmpty(100)
    //stop
}
