package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.MariaType
import dev.typr.foundations.MariaTypes

@Suppress("unused")
class IntegerTypesSigned {
    //start
    val tinyType: MariaType<Byte> = MariaTypes.tinyint
    val intType: MariaType<Int> = MariaTypes.int_
    val bigType: MariaType<Long> = MariaTypes.bigint
    //stop
}
