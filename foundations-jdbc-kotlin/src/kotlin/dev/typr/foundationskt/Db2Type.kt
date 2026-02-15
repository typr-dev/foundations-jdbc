@file:Suppress("unused")
package dev.typr.foundationskt

class Db2Type<T>(override val underlying: dev.typr.foundations.Db2Type<T>) : DbType<T>(underlying) {
    override fun opt(): Db2Type<T?> =
        Db2Type(underlying.opt().to(Bijection.optionalToNullableUnchecked()))

    override fun <B> to(bijection: dev.typr.foundations.Bijection<T, B>): Db2Type<B> =
        Db2Type(underlying.to(bijection))

    fun <B> bimap(f: dev.typr.foundations.SqlFunction<T, B>, g: (B) -> T): Db2Type<B> =
        Db2Type(underlying.bimap(f, g))

    fun unchecked(): Db2Type<T> = Db2Type(underlying.unchecked())
    fun nullableOk(): Db2Type<T> = Db2Type(underlying.nullableOk())
}
