package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*

@Suppress("unused")
class NestedCollections {
    //start
    // Nested collections compose — any combination is legal.
    val grid: DuckDbType<List<List<Int>>> = DuckDbTypes.integer.list().list()
    val matrix: DuckDbType<List<List<Float>>> = DuckDbTypes.float_.array(3).array(3)
    val variableRows: DuckDbType<List<List<String>>> = DuckDbTypes.varchar.list().array(4)
    //stop
}
