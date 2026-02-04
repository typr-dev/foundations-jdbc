package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class FloatingPointTypes {
    //start
    val realType: SqlServerType<Float> = SqlServerTypes.real
    val floatType: SqlServerType<Double> = SqlServerTypes.float_
    //stop
}
