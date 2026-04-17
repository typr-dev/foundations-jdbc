@file:Suppress("unused")
package dev.typr.foundationskt

import dev.typr.foundations.AnalysisOptions
import dev.typr.foundations.DuckDbJson
import dev.typr.foundations.DuckDbListCodec
import dev.typr.foundations.DuckDbRead
import dev.typr.foundations.DuckDbStringifier
import dev.typr.foundations.DuckDbTypename
import dev.typr.foundations.DuckDbWrite

class DuckDbType<T>(override val underlying: dev.typr.foundations.DuckDbType<T>) : DbType<T>(underlying) {
    override fun opt(): DuckDbType<T?> =
        DuckDbType(underlying.opt().to(Bijection.optionalToNullable()))

    override fun <B> to(bijection: dev.typr.foundations.Bijection<T, B>): DuckDbType<B> =
        DuckDbType(underlying.to(bijection))

    fun <B> transform(f: (T) -> B, g: (B) -> T): DuckDbType<B> =
        DuckDbType(underlying.transform({ f(it) }, g))

    fun <V> mapTo(valueType: DuckDbType<V>): DuckDbType<Map<T, V>> =
        DuckDbType(underlying.mapTo(valueType.underlying))

    /**
     * Fixed-size ARRAY of this type ({@code T[size]} in DuckDB). Every row has exactly {@code
     * size} elements. Use {@link #list} for variable-length lists.
     */
    fun array(size: Int): DuckDbType<List<T>> = DuckDbType(underlying.array(size))

    fun list(): DuckDbType<List<T>> = DuckDbType(underlying.list())

    fun encode(value: T): dev.typr.foundations.Fragment.Value<T> = underlying.encode(value)

    fun withTypename(typename: DuckDbTypename<T>): DuckDbType<T> = DuckDbType(underlying.withTypename(typename))
    fun withTypename(sqlType: String): DuckDbType<T> = DuckDbType(underlying.withTypename(sqlType))
    fun renamed(value: String): DuckDbType<T> = DuckDbType(underlying.renamed(value))
    fun renamedDropPrecision(value: String): DuckDbType<T> = DuckDbType(underlying.renamedDropPrecision(value))

    fun withRead(read: DuckDbRead<T>): DuckDbType<T> = DuckDbType(underlying.withRead(read))
    fun withWrite(write: DuckDbWrite<T>): DuckDbType<T> = DuckDbType(underlying.withWrite(write))
    fun withStringifier(stringifier: DuckDbStringifier<T>): DuckDbType<T> = DuckDbType(underlying.withStringifier(stringifier))
    fun withJson(json: DuckDbJson<T>): DuckDbType<T> = DuckDbType(underlying.withJson(json))
    fun withAnalysis(opts: AnalysisOptions): DuckDbType<T> = DuckDbType(underlying.withAnalysis(opts))
    fun withListCodec(codec: DuckDbListCodec<T>): DuckDbType<T> = DuckDbType(underlying.withListCodec(codec))

    fun unchecked(): DuckDbType<T> = DuckDbType(underlying.unchecked())
    fun nullableOk(): DuckDbType<T> = DuckDbType(underlying.nullableOk())
}
