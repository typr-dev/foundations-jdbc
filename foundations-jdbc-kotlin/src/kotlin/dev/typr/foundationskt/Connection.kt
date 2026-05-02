@file:Suppress("unused")
package dev.typr.foundationskt

open class ConnectionRead(val underlying: dev.typr.foundations.ConnectionRead) {

    fun <T> execute(op: OperationRead<T>): T =
        underlying.execute(op.underlying)

    fun <T : Any> query(sql: Fragment, codec: RowCodec<T>): List<T> =
        underlying.query(sql.underlying, codec.underlying)

    fun <T : Any> queryFirst(sql: Fragment, codec: RowCodec<T>): T? =
        underlying.queryFirst(sql.underlying, codec.underlying).orElse(null)
}

class Connection(private val java: dev.typr.foundations.Connection) : ConnectionRead(java) {

    val javaConnection: dev.typr.foundations.Connection get() = java

    fun <T> execute(op: Operation<T>): T =
        java.execute(op.underlying)

    fun update(sql: Fragment): Int =
        java.update(sql.underlying)

    fun unwrap(): java.sql.Connection =
        java.unwrap()
}
