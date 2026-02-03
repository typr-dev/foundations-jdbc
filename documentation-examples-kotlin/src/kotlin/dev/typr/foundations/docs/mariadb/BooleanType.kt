package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.MariaType
import dev.typr.foundations.MariaTypes

@Suppress("unused")
class BooleanType {
    //start
    val boolType: MariaType<Boolean> = MariaTypes.bool
    val bitBool: MariaType<Boolean> = MariaTypes.bit1
    //stop
}
