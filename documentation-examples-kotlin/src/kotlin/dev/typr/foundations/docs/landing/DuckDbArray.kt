package dev.typr.foundations.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class DuckDbArray {
    lateinit var tx: Transactor

    //start
    // DuckDB arrays are first-class typed values
    fun getTagSets(): List<Array<String>> =
        sql { "SELECT tags FROM posts WHERE published = true" }
            .query(RowCodec.of(DuckDbTypes.varcharArray).all())
            .transact(tx)
    //stop
}
