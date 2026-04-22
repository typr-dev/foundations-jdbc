@file:Suppress("unused")
package dev.typr.foundationskt

import dev.typr.foundations.AnalysisOptions
import dev.typr.foundations.PgElementCodec
import dev.typr.foundations.PgJson
import dev.typr.foundations.PgOutParam
import dev.typr.foundations.PgRead
import dev.typr.foundations.PgText
import dev.typr.foundations.PgTypename
import dev.typr.foundations.PgWrite

class PgType<T>(override val underlying: dev.typr.foundations.PgType<T>) : DbType<T>(underlying) {
    override fun opt(): PgType<T?> =
        PgType(underlying.opt().to(Bijection.optionalToNullable()))

    override fun <B> to(bijection: Bijection<T, B>): PgType<B> =
        PgType(underlying.to(bijection.underlying))

    fun <B> transform(f: (T) -> B, g: (B) -> T): PgType<B> =
        PgType(underlying.transform(dev.typr.foundations.SqlFunction { f(it) }, g))

    /** Variable-length PG array of this type — Kotlin-side as {@code List<T>}. */
    fun array(): PgType<List<T>> = PgType(underlying.array())

    fun encode(value: T): Fragment = Fragment(underlying.encode(value))

    fun pgText(): PgText<T> = underlying.pgText()

    fun withTypename(typename: PgTypename<T>): PgType<T> = PgType(underlying.withTypename(typename))
    fun withTypename(sqlType: String): PgType<T> = PgType(underlying.withTypename(sqlType))
    fun renamed(value: String): PgType<T> = PgType(underlying.renamed(value))
    fun renamedDropPrecision(value: String): PgType<T> = PgType(underlying.renamedDropPrecision(value))

    fun withRead(read: PgRead<T>): PgType<T> = PgType(underlying.withRead(read))
    fun withWrite(write: PgWrite<T>): PgType<T> = PgType(underlying.withWrite(write))
    fun withText(text: PgText<T>): PgType<T> = PgType(underlying.withText(text))
    fun withJson(json: PgJson<T>): PgType<T> = PgType(underlying.withJson(json))
    fun withOutParam(outParam: PgOutParam<T>): PgType<T> = PgType(underlying.withOutParam(outParam))
    fun withArrayCodec(codec: PgElementCodec<T>): PgType<T> = PgType(underlying.withArrayCodec(codec))
    fun withAnalysis(opts: AnalysisOptions): PgType<T> = PgType(underlying.withAnalysis(opts))

    fun unchecked(): PgType<T> = PgType(underlying.unchecked())
    fun nullableOk(): PgType<T> = PgType(underlying.nullableOk())
}
