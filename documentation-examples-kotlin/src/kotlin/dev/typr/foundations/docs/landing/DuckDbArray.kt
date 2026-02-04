package dev.typr.foundations.docs.landing

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.sql.Connection

@Suppress("unused")
class DuckDbArray {
    val tx: Transactor? = null // placeholder

    //start
    // DuckDB arrays are first-class typed values
    fun getTagSets(): List<Array<String>> = tx!!.execute(SqlFunction { conn: Connection ->
        Fragment.lit("SELECT tags FROM posts WHERE published = true")
            .query(RowParser.of(DuckDbTypes.varcharArray).all())
            .run(conn)
    })
    //stop
}
