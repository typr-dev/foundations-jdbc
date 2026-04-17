package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*

@Suppress("unused")
class ArrayTypes {
    //start
    // Fixed-size ARRAY — every row must have exactly `size` elements.
    val embedding: DuckDbType<List<Float>> = DuckDbTypes.float_.array(1536)
    val rgb: DuckDbType<List<Int>> = DuckDbTypes.integer.array(3)
    //stop
}
