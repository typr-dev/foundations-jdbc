package dev.typr.foundationskt.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class FloatingPointTypes {
    //start
    val realType: SqlServerType<Float> = SqlServerTypes.real
    val floatType: SqlServerType<Double> = SqlServerTypes.float_
    //stop
}
