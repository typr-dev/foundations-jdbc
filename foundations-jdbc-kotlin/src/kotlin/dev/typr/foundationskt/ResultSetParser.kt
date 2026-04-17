package dev.typr.foundationskt

import java.sql.ResultSet

class ResultSetParser<Out>(val underlying: dev.typr.foundations.ResultSetParser<Out>) {
    fun apply(rs: ResultSet): Out = underlying.apply(rs)

    fun <Out2> map(f: (Out) -> Out2): ResultSetParser<Out2> =
        ResultSetParser(underlying.map(f))

    companion object {
        /**
         * Parse every row as a single column of the given type.
         *
         * `type` may be nullable — passing `PgTypes.text.opt()` yields
         * `ResultSetParser<List<String?>>` with proper null handling per row.
         */
        fun <T> all(type: DbType<T>): ResultSetParser<List<T>> =
            ResultSetParser(dev.typr.foundations.RowCodec.of(type.underlying).all().map { it.toList() })

        /**
         * Parse exactly one row as a single column. Throws if zero or more than one row.
         *
         * `type` may be nullable; the returned value reflects whether the column was NULL.
         */
        fun <T> exactlyOne(type: DbType<T>): ResultSetParser<T> =
            ResultSetParser(dev.typr.foundations.RowCodec.of(type.underlying).exactlyOne())

        /**
         * Parse at most one row as a single column, or null if zero rows.
         *
         * With `type` nullable, the returned `T?` conflates "no row" and "one NULL row".
         * Use [all] if you need to distinguish.
         */
        fun <T : Any> maxOne(type: DbType<T>): ResultSetParser<T?> =
            ResultSetParser(dev.typr.foundations.RowCodec.of(type.underlying).maxOne().map { it.orElse(null) })

        /**
         * Parse the first row as a single column, or null if zero rows.
         */
        fun <T : Any> first(type: DbType<T>): ResultSetParser<T?> =
            ResultSetParser(dev.typr.foundations.RowCodec.of(type.underlying).first().map { it.orElse(null) })
    }
}
