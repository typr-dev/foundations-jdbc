package dev.typr.foundations.docs.oracle

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class NullableType {
    //start
    val notNull: OracleType<Int> = OracleTypes.numberInt
    val nullable: OracleType<Int?> = OracleTypes.numberInt.opt()
    //stop
}
