package dev.typr.foundations.docs.oracle

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.util.Optional

@Suppress("unused")
class NullableType {
    //start
    val notNull: OracleType<Int> = OracleTypes.numberInt
    val nullable: OracleType<Optional<Int>> = OracleTypes.numberInt.opt()
    //stop
}
