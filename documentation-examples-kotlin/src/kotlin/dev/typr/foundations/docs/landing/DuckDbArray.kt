package dev.typr.foundations.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class DuckDbArray {
    val tx: Transactor? = null // placeholder

    //start
    // DuckDB arrays are first-class typed values
    fun getTagSets(): List<Array<String>> = Sql { "SELECT tags FROM posts WHERE published = true" }
        .query(RowParser.of(DuckDbTypes.varcharArray).all())
        .transact(tx!!)
    //stop
}
