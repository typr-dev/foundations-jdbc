package dev.typr.foundationskt.docs.db2

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class IntegerTypes {
    //start
    val smallType: Db2Type<Short> = Db2Types.smallint
    val intType: Db2Type<Int> = Db2Types.integer
    val bigType: Db2Type<Long> = Db2Types.bigint
    //stop
}
