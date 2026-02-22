package dev.typr.foundationskt.docs.db2

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class NullableType {
    //start
    val notNull: Db2Type<Int> = Db2Types.integer
    val nullable: Db2Type<Int?> = Db2Types.integer.opt()
    //stop
}
