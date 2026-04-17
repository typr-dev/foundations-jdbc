package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class StringTypes {
    //start
    val textType: PgType<String> = PgTypes.text
    val charType: PgType<String> = PgTypes.bpcharOf(10)  // char(10)
    //stop
}
