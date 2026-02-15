package dev.typr.foundationskt

import java.sql.ResultSet

class ResultSetParser<Out>(val underlying: dev.typr.foundations.ResultSetParser<Out>) {
    fun apply(rs: ResultSet): Out = underlying.apply(rs)
}
