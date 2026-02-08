package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.util.Optional

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: DuckDbType<Int> = DuckDbTypes.integer
    val nullable: DuckDbType<Optional<Int>> = DuckDbTypes.integer.opt()
    //stop
}
