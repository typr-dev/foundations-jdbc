@file:Suppress("unused")
package dev.typr.foundationskt

open class DbType<T>(open val underlying: dev.typr.foundations.DbType<T>) {
    open fun opt(): DbType<T?> =
        DbType(underlying.opt().to(Bijection.optionalToNullable()))

    open fun <B> to(bijection: dev.typr.foundations.Bijection<T, B>): DbType<B> =
        DbType(underlying.to(bijection))

    /** Whether this type allows null values. True for types created with [opt]. */
    fun isNullable(): Boolean = underlying.isNullable

    operator fun invoke(value: T): Fragment =
        Fragment(underlying.apply(value))

    override fun toString(): String = underlying.toString()
}
