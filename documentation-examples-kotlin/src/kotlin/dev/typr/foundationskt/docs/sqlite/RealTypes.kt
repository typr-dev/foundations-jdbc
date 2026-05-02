package dev.typr.foundationskt.docs.sqlite

import dev.typr.foundationskt.*

@Suppress("unused")
class RealTypes {
    //start
    val realType: SqliteType<Double> = SqliteTypes.real
    val doubleType: SqliteType<Double> = SqliteTypes.double_
    val floatType: SqliteType<Float> = SqliteTypes.float_
    //stop
}
