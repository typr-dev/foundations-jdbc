package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: DuckDbType<Int> = DuckDbTypes.integer
    val nullable: DuckDbType<Int?> = DuckDbTypes.integer.opt()
    //stop
}
