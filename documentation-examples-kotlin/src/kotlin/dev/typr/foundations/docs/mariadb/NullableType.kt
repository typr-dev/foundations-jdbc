package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class NullableType {
    //start
    val notNull: MariaType<Int> = MariaTypes.int_
    val nullable: MariaType<Int?> = MariaTypes.int_.opt()
    //stop
}
