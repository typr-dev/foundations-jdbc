package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal

@Suppress("unused")
class NumericTypes {
    //start
    val numberType: OracleType<BigDecimal> = OracleTypes.number
    val decimal: OracleType<BigDecimal> = OracleTypes.number(10, 2)  // NUMBER(10,2)
    val intType: OracleType<Int> = OracleTypes.numberAsInt(9)        // NUMBER(9)
    val longType: OracleType<Long> = OracleTypes.numberAsLong(18)    // NUMBER(18)
    //stop
}
