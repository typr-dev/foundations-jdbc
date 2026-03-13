@file:Suppress("unused")
package dev.typr.foundationskt

class OracleType<T>(override val underlying: dev.typr.foundations.OracleType<T>) : DbType<T>(underlying) {
    override fun opt(): OracleType<T?> =
        OracleType(underlying.opt().to(Bijection.optionalToNullable()))

    override fun <B> to(bijection: dev.typr.foundations.Bijection<T, B>): OracleType<B> =
        OracleType(underlying.to(bijection))

    fun <B> transform(f: dev.typr.foundations.SqlFunction<T, B>, g: (B) -> T): OracleType<B> =
        OracleType(underlying.transform(f, g))

    fun unchecked(): OracleType<T> = OracleType(underlying.unchecked())
    fun nullableOk(): OracleType<T> = OracleType(underlying.nullableOk())
}
