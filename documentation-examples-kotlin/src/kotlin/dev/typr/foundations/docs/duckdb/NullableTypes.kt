package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbOptType
import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: DuckDbType<Int> = DuckDbTypes.integer
    val nullable: DuckDbOptType<Int> = DuckDbTypes.integer.opt()
    //stop
}
