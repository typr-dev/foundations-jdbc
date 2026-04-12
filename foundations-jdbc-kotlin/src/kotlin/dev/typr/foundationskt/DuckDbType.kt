@file:Suppress("unused")
package dev.typr.foundationskt

import dev.typr.foundations.AnalysisOptions
import dev.typr.foundations.DuckDbJson
import dev.typr.foundations.DuckDbRead
import dev.typr.foundations.DuckDbStringifier
import dev.typr.foundations.DuckDbTypename
import dev.typr.foundations.DuckDbWrite
import java.util.function.IntFunction

class DuckDbType<T>(override val underlying: dev.typr.foundations.DuckDbType<T>) : DbType<T>(underlying) {
    override fun opt(): DuckDbType<T?> =
        DuckDbType(underlying.opt().to(Bijection.optionalToNullable()))

    override fun <B> to(bijection: dev.typr.foundations.Bijection<T, B>): DuckDbType<B> =
        DuckDbType(underlying.to(bijection))

    fun <B> transform(f: (T) -> B, g: (B) -> T): DuckDbType<B> =
        DuckDbType(underlying.transform(dev.typr.foundations.SqlFunction { f(it) }, g))

    fun <V> mapTo(valueType: DuckDbType<V>): DuckDbType<Map<T, V>> =
        DuckDbType(underlying.mapTo(valueType.underlying))

    fun array(): DuckDbType<Array<T>> = DuckDbType(underlying.array())

    fun list(): DuckDbType<List<T>> = DuckDbType(underlying.list())

    fun listNative(elementClass: Class<T>, toArray: IntFunction<Array<T?>>): DuckDbType<List<T>> =
        DuckDbType(underlying.listNative(elementClass, toArray))

    fun listViaSqlLiteral(elementClass: Class<T>, elementStringifier: DuckDbStringifier<T>): DuckDbType<List<T>> =
        DuckDbType(underlying.listViaSqlLiteral(elementClass, elementStringifier))

    fun <W> listViaSqlLiteral(wireClass: Class<W>, wireToElem: (W) -> T, elementStringifier: DuckDbStringifier<T>): DuckDbType<List<T>> =
        DuckDbType(underlying.listViaSqlLiteral(wireClass, { wireToElem(it) }, elementStringifier))

    fun arrayNative(size: Int, elementClass: Class<T>, toArray: IntFunction<Array<T?>>): DuckDbType<List<T>> =
        DuckDbType(underlying.arrayNative(size, elementClass, toArray))

    fun arrayViaSqlLiteral(size: Int, elementClass: Class<T>, elementStringifier: DuckDbStringifier<T>): DuckDbType<List<T>> =
        DuckDbType(underlying.arrayViaSqlLiteral(size, elementClass, elementStringifier))

    fun <V> mapToNative(valueType: DuckDbType<V>, keyClass: Class<T>, valueClass: Class<V>): DuckDbType<Map<T, V>> =
        DuckDbType(underlying.mapToNative(valueType.underlying, keyClass, valueClass))

    fun <V> mapToViaSqlLiteral(
        valueType: DuckDbType<V>,
        keyClass: Class<T>,
        valueClass: Class<V>,
        keyStringifier: DuckDbStringifier<T>,
        valueStringifier: DuckDbStringifier<V>
    ): DuckDbType<Map<T, V>> =
        DuckDbType(underlying.mapToViaSqlLiteral(valueType.underlying, keyClass, valueClass, keyStringifier, valueStringifier))

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

    fun noArraySupport(): DuckDbType<T> = DuckDbType(underlying.noArraySupport())

    fun unchecked(): DuckDbType<T> = DuckDbType(underlying.unchecked())
    fun nullableOk(): DuckDbType<T> = DuckDbType(underlying.nullableOk())
}
