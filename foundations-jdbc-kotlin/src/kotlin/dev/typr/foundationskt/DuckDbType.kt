@file:Suppress("unused")
package dev.typr.foundationskt

class DuckDbType<T>(override val underlying: dev.typr.foundations.DuckDbType<T>) : DbType<T>(underlying) {
    override fun opt(): DuckDbType<T?> =
        DuckDbType(underlying.opt().to(Bijection.optionalToNullable()))

    override fun <B> to(bijection: dev.typr.foundations.Bijection<T, B>): DuckDbType<B> =
        DuckDbType(underlying.to(bijection))

    fun <B> transform(f: (T) -> B, g: (B) -> T): DuckDbType<B> =
        DuckDbType(underlying.transform(dev.typr.foundations.SqlFunction { f(it) }, g))

    fun <V> mapTo(valueType: DuckDbType<V>): DuckDbType<Map<T, V>> =
        DuckDbType(underlying.mapTo(valueType.underlying))

    fun list(): DuckDbType<List<T>> = DuckDbType(underlying.list())

    fun unchecked(): DuckDbType<T> = DuckDbType(underlying.unchecked())
    fun nullableOk(): DuckDbType<T> = DuckDbType(underlying.nullableOk())
}
