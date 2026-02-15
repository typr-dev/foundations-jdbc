@file:Suppress("unused")
package dev.typr.kotlinfoundations

class MariaType<T>(override val underlying: dev.typr.foundations.MariaType<T>) : DbType<T>(underlying) {
    override fun opt(): MariaType<T?> =
        MariaType(underlying.opt().to(Bijection.optionalToNullableUnchecked()))

    override fun <B> to(bijection: dev.typr.foundations.Bijection<T, B>): MariaType<B> =
        MariaType(underlying.to(bijection))

    fun <B> bimap(f: dev.typr.foundations.SqlFunction<T, B>, g: (B) -> T): MariaType<B> =
        MariaType(underlying.bimap(f, g))

    fun unchecked(): MariaType<T> = MariaType(underlying.unchecked())
    fun nullableOk(): MariaType<T> = MariaType(underlying.nullableOk())
}
