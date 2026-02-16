@file:Suppress("unused")
package dev.typr.foundationskt

class SqlServerType<T>(override val underlying: dev.typr.foundations.SqlServerType<T>) : DbType<T>(underlying) {
    override fun opt(): SqlServerType<T?> =
        SqlServerType(underlying.opt().to(Bijection.optionalToNullableUnchecked()))

    override fun <B> to(bijection: dev.typr.foundations.Bijection<T, B>): SqlServerType<B> =
        SqlServerType(underlying.to(bijection))

    fun <B> transform(f: dev.typr.foundations.SqlFunction<T, B>, g: (B) -> T): SqlServerType<B> =
        SqlServerType(underlying.transform(f, g))

    fun unchecked(): SqlServerType<T> = SqlServerType(underlying.unchecked())
    fun nullableOk(): SqlServerType<T> = SqlServerType(underlying.nullableOk())
}
