package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: DuckDbType<Int> = DuckDbTypes.integer
    val nullable: DuckDbType<Int?> = DuckDbTypes.integer.opt()
    //stop
}
