package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes

@Suppress("unused")
class FloatingPointTypes {
    //start
    val realType: SqlServerType<Float> = SqlServerTypes.real
    val floatType: SqlServerType<Double> = SqlServerTypes.float_
    //stop
}
