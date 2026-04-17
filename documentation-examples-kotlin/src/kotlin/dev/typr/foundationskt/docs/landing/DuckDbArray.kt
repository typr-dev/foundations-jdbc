package dev.typr.foundationskt.docs.landing

import dev.typr.foundationskt.*

@Suppress("unused")
class DuckDbArray {
    lateinit var tx: Transactor

    //start
    // DuckDB LIST columns are first-class typed values
    fun getTagSets(): List<List<String>> =
        sql { "SELECT tags FROM posts WHERE published = true" }
            .query(RowCodec.of(DuckDbTypes.varchar.list()).all())
            .transact(tx)
    //stop
}
