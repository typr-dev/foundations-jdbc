package dev.typr.foundations.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class StringTypes {
    //start
    val varcharType: DuckDbType<String> = DuckDbTypes.varchar
    val charType: DuckDbType<String> = DuckDbTypes.char_(10)
    //stop
}
