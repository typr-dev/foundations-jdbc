package dev.typr.foundations.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class StringTypes {
    //start
    val charType: MariaType<String> = MariaTypes.char_(10)
    val varcharType: MariaType<String> = MariaTypes.varchar(255)
    val textType: MariaType<String> = MariaTypes.text
    val longType: MariaType<String> = MariaTypes.longtext
    //stop
}
