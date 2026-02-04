package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class StringTypes {
    //start
    val varcharType: DuckDbType<String> = DuckDbTypes.varchar
    val charType: DuckDbType<String> = DuckDbTypes.char_(10)
    //stop
}
