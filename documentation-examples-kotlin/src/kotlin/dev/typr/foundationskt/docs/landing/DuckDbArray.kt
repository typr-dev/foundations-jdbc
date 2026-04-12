package dev.typr.foundationskt.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class DuckDbArray {
    lateinit var tx: Transactor

    //start
    // DuckDB arrays are first-class typed values
    fun getTagSets(): List<Array<String>> =
        sql { "SELECT tags FROM posts WHERE published = true" }
            .query(RowCodec.of(DuckDbTypes.varchar.array()).all())
            .transact(tx)
    //stop
}
