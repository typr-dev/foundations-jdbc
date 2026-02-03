package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes

@Suppress("unused")
class FloatingPointTypes {
    //start
    val floatType: DuckDbType<Float> = DuckDbTypes.float_
    val doubleType: DuckDbType<Double> = DuckDbTypes.double_
    //stop
}
