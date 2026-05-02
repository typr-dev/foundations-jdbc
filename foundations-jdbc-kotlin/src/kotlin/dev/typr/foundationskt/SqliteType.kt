@file:Suppress("unused")
package dev.typr.foundationskt

import dev.typr.foundations.AnalysisOptions
import dev.typr.foundations.SqliteJson
import dev.typr.foundations.SqliteRead
import dev.typr.foundations.SqliteTypename
import dev.typr.foundations.SqliteWrite

class SqliteType<T>(override val underlying: dev.typr.foundations.SqliteType<T>) : DbType<T>(underlying) {
    override fun opt(): SqliteType<T?> =
        SqliteType(underlying.opt().to(Bijection.optionalToNullable()))

    override fun <B> to(bijection: Bijection<T, B>): SqliteType<B> =
        SqliteType(underlying.to(bijection.underlying))

    fun <B> transform(f: (T) -> B, g: (B) -> T): SqliteType<B> =
        SqliteType(underlying.transform({ f(it) }, g))

    fun encode(value: T): Fragment = Fragment(underlying.encode(value))

    fun withTypename(typename: SqliteTypename<T>): SqliteType<T> = SqliteType(underlying.withTypename(typename))
    fun withTypename(sqlType: String): SqliteType<T> = SqliteType(underlying.withTypename(sqlType))
    fun renamed(value: String): SqliteType<T> = SqliteType(underlying.renamed(value))
    fun renamedDropPrecision(value: String): SqliteType<T> = SqliteType(underlying.renamedDropPrecision(value))

    fun withRead(read: SqliteRead<T>): SqliteType<T> = SqliteType(underlying.withRead(read))
    fun withWrite(write: SqliteWrite<T>): SqliteType<T> = SqliteType(underlying.withWrite(write))
    fun withJson(json: SqliteJson<T>): SqliteType<T> = SqliteType(underlying.withJson(json))
    fun withAnalysis(opts: AnalysisOptions): SqliteType<T> = SqliteType(underlying.withAnalysis(opts))

    fun unchecked(): SqliteType<T> = SqliteType(underlying.unchecked())
    fun nullableOk(): SqliteType<T> = SqliteType(underlying.nullableOk())
}
