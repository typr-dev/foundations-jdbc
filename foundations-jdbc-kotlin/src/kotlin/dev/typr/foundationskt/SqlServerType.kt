@file:Suppress("unused")
package dev.typr.foundationskt

class SqlServerType<T>(override val underlying: dev.typr.foundations.SqlServerType<T>) : DbType<T>(underlying) {
    override fun opt(): SqlServerType<T?> =
        SqlServerType(underlying.opt().to(Bijection.optionalToNullable()))

    override fun <B> to(bijection: Bijection<T, B>): SqlServerType<B> =
        SqlServerType(underlying.to(bijection.underlying))

    fun <B> transform(f: (T) -> B, g: (B) -> T): SqlServerType<B> =
        SqlServerType(underlying.transform(dev.typr.foundations.SqlFunction { f(it) }, g))

    fun unchecked(): SqlServerType<T> = SqlServerType(underlying.unchecked())
    fun nullableOk(): SqlServerType<T> = SqlServerType(underlying.nullableOk())
}
