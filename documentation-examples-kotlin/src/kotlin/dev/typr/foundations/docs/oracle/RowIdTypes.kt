package dev.typr.foundations.docs.oracle

import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes

@Suppress("unused")
class RowIdTypes {
    //start
    val rowidType: OracleType<String> = OracleTypes.rowId
    val urowidType: OracleType<String> = OracleTypes.uRowId
    val urowid1000: OracleType<String> = OracleTypes.uRowId(1000)
    //stop
}
