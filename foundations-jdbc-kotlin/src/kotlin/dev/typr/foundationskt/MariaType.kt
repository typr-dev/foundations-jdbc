@file:Suppress("unused")
package dev.typr.foundationskt

class MariaType<T>(override val underlying: dev.typr.foundations.MariaType<T>) : DbType<T>(underlying) {
    override fun opt(): MariaType<T?> =
        MariaType(underlying.opt().to(Bijection.optionalToNullable()))

    override fun <B> to(bijection: Bijection<T, B>): MariaType<B> =
        MariaType(underlying.to(bijection.underlying))

    fun <B> transform(f: (T) -> B, g: (B) -> T): MariaType<B> =
        MariaType(underlying.transform(dev.typr.foundations.SqlFunction { f(it) }, g))

    fun unchecked(): MariaType<T> = MariaType(underlying.unchecked())
    fun nullableOk(): MariaType<T> = MariaType(underlying.nullableOk())
}
