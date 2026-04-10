package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.util.UUID

@Suppress("unused")
class ArrayTypes {
    //start
    // Any type can be made into an array with .array()
    val intArray: DuckDbType<Array<Int>> = DuckDbTypes.integer.array()
    val strArray: DuckDbType<Array<String>> = DuckDbTypes.varchar.array()
    val uuidArray: DuckDbType<Array<UUID>> = DuckDbTypes.uuid.array()
    //stop
}
