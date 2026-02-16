@file:Suppress("unused")
package dev.typr.foundationskt

open class DbType<T>(open val underlying: dev.typr.foundations.DbType<T>) {
    open fun opt(): DbType<T?> =
        DbType(underlying.opt().to(Bijection.optionalToNullableUnchecked()))

    open fun <B> to(bijection: dev.typr.foundations.Bijection<T, B>): DbType<B> =
        DbType(underlying.to(bijection))
}
