package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.MariaType
import dev.typr.foundations.MariaTypes
import java.util.Optional

@Suppress("unused")
class NullableType {
    //start
    val notNull: MariaType<Int> = MariaTypes.int_
    val nullable: MariaType<Optional<Int>> = MariaTypes.int_.opt()
    //stop
}
