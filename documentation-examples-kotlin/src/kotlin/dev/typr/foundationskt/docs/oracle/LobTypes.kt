package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class LobTypes {
    //start
    val clobType: OracleType<String> = OracleTypes.clob
    val nclobType: OracleType<String> = OracleTypes.nclob
    val blobType: OracleType<ByteArray> = OracleTypes.blob

    // Non-empty variants
    val clobNonEmpty: OracleType<NonEmptyString> = OracleTypes.clobNonEmpty
    val blobNonEmpty: OracleType<NonEmptyBlob> = OracleTypes.blobNonEmpty
    //stop
}
