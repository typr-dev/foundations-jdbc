package dev.typr.foundationskt.docs.sqlite

import dev.typr.foundationskt.*

@Suppress("unused")
class StringTypes {
    //start
    val textType: SqliteType<String> = SqliteTypes.text
    val varcharType: SqliteType<String> = SqliteTypes.varcharOf(255)
    val charType: SqliteType<String> = SqliteTypes.charOf(10)
    val clobType: SqliteType<String> = SqliteTypes.clob
    //stop
}
