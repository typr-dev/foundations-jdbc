package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes
import java.util.UUID

@Suppress("unused")
class ArrayTypes {
    //start
    val intArray: DuckDbType<Array<Int>> = DuckDbTypes.integerArray
    val strArray: DuckDbType<Array<String>> = DuckDbTypes.varcharArray
    val uuidArray: DuckDbType<Array<UUID>> = DuckDbTypes.uuidArray

    // Create array for any type
    // val customArray: DuckDbType<Array<MyType>> = myType.array()
    //stop
}
