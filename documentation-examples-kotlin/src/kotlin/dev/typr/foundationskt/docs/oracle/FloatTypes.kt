package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class FloatTypes {
    //start
    val binaryFloat: OracleType<Float> = OracleTypes.binaryFloat
    val binaryDouble: OracleType<Double> = OracleTypes.binaryDouble
    val floatType: OracleType<Double> = OracleTypes.float_Of(126)  // FLOAT(126)
    //stop
}
