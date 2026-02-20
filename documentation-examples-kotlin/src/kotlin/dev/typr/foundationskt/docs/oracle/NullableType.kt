package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class NullableType {
    //start
    val notNull: OracleType<Int> = OracleTypes.numberInt
    val nullable: OracleType<Int?> = OracleTypes.numberInt.opt()
    //stop
}
