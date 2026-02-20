package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class IntegerTypesSigned {
    //start
    val tinyType: MariaType<Byte> = MariaTypes.tinyint
    val intType: MariaType<Int> = MariaTypes.int_
    val bigType: MariaType<Long> = MariaTypes.bigint
    //stop
}
