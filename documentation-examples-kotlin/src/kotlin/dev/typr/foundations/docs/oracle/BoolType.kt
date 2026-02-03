package dev.typr.foundations.docs.oracle

import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes

@Suppress("unused")
class BoolType {
    //start
    val boolNative: OracleType<Boolean> = OracleTypes.boolean_        // Oracle 23c+
    val boolNumber: OracleType<Boolean> = OracleTypes.numberAsBoolean // NUMBER(1)
    //stop
}
