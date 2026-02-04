package dev.typr.foundations.docs.oracle

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class RowIdTypes {
    //start
    val rowidType: OracleType<String> = OracleTypes.rowId
    val urowidType: OracleType<String> = OracleTypes.uRowId
    val urowid1000: OracleType<String> = OracleTypes.uRowId(1000)
    //stop
}
