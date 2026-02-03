package dev.typr.foundations.docs.oracle

import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes
import java.util.Optional

@Suppress("unused")
class NullableType {
    //start
    val notNull: OracleType<Int> = OracleTypes.numberInt
    val nullable: OracleType<Optional<Int>> = OracleTypes.numberInt.opt()
    //stop
}
