package dev.typr.foundations.docs.oracle

import dev.typr.foundations.NonEmptyBlob
import dev.typr.foundations.NonEmptyString
import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes

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
