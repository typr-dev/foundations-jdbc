package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class IntegerTypesSigned {
    //start
    val tinyType: MariaType<Byte> = MariaTypes.tinyint
    val intType: MariaType<Int> = MariaTypes.int_
    val bigType: MariaType<Long> = MariaTypes.bigint
    //stop
}
