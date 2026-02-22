package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class NullableType {
    //start
    val notNull: MariaType<Int> = MariaTypes.int_
    val nullable: MariaType<Int?> = MariaTypes.int_.opt()
    //stop
}
