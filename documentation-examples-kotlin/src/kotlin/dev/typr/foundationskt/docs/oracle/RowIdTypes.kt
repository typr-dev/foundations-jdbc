package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class RowIdTypes {
    //start
    val rowidType: OracleType<String> = OracleTypes.rowId
    val urowidType: OracleType<String> = OracleTypes.uRowId
    val urowid1000: OracleType<String> = OracleTypes.uRowId(1000)
    //stop
}
