package dev.typr.foundations.docs.landing

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class DuckDbArray {
    val tx: Transactor? = null // placeholder

    //start
    // DuckDB arrays are first-class typed values
    fun getTagSets(): List<Array<String>> = Fragment.lit("SELECT tags FROM posts WHERE published = true")
        .query(RowParser.of(DuckDbTypes.varcharArray).all())
        .transact(tx!!)
    //stop
}
