package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes

@Suppress("unused")
class StringTypes {
    //start
    val textType: PgType<String> = PgTypes.text
    val charType: PgType<String> = PgTypes.bpchar(10)  // char(10)
    //stop
}
