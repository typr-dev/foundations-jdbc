package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class BoolType {
    //start
    val boolNative: OracleType<Boolean> = OracleTypes.boolean_        // Oracle 23c+
    val boolNumber: OracleType<Boolean> = OracleTypes.numberAsBoolean // NUMBER(1)
    //stop
}
