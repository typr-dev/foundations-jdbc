package dev.typr.foundations.docs.db2

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class FloatingPointTypes {
    //start
    val realType: Db2Type<Float> = Db2Types.real
    val doubleType: Db2Type<Double> = Db2Types.double_
    //stop
}
