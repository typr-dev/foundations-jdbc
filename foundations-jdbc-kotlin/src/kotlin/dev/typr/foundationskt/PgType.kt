@file:Suppress("unused")
package dev.typr.foundationskt

class PgType<T>(override val underlying: dev.typr.foundations.PgType<T>) : DbType<T>(underlying) {
    override fun opt(): PgType<T?> =
        PgType(underlying.opt().to(Bijection.optionalToNullableUnchecked()))

    override fun <B> to(bijection: dev.typr.foundations.Bijection<T, B>): PgType<B> =
        PgType(underlying.to(bijection))

    fun <B> bimap(f: dev.typr.foundations.SqlFunction<T, B>, g: (B) -> T): PgType<B> =
        PgType(underlying.bimap(f, g))

    fun pgText(): dev.typr.foundations.PgText<T> = underlying.pgText()

    fun unchecked(): PgType<T> = PgType(underlying.unchecked())
    fun nullableOk(): PgType<T> = PgType(underlying.nullableOk())
}
