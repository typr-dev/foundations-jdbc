package dev.typr.foundations.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class BooleanType {
    //start
    val boolType: MariaType<Boolean> = MariaTypes.bool
    val bitBool: MariaType<Boolean> = MariaTypes.bit1
    //stop
}
