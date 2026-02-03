package dev.typr.foundations.docs.landing

import dev.typr.foundations.DuckDbTypes
import dev.typr.foundations.Fragment
import dev.typr.foundations.kotlin.RowParser
import dev.typr.foundations.kotlin.query
import dev.typr.foundations.SqlFunction
import dev.typr.foundations.Transactor
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
