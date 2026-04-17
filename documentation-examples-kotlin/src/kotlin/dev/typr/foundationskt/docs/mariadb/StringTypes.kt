package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class StringTypes {
    //start
    val charType: MariaType<String> = MariaTypes.char_Of(10)
    val varcharType: MariaType<String> = MariaTypes.varcharOf(255)
    val textType: MariaType<String> = MariaTypes.text
    val longType: MariaType<String> = MariaTypes.longtext
    //stop
}
