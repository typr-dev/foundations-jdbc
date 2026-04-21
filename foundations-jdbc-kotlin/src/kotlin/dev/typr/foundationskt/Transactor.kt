@file:Suppress("unused")
package dev.typr.foundationskt

class Transactor(val underlying: dev.typr.foundations.Transactor) : AutoCloseable {

    fun <T> execute(operation: Operation<T>): T =
        underlying.execute(operation.underlying)

    fun <T> transact(f: (Connection) -> T): T =
        underlying.transact { javaConn -> f(Connection(javaConn)) }

    fun <T> transactRead(f: (ConnectionRead) -> T): T =
        underlying.transactRead { javaConn -> f(ConnectionRead(javaConn)) }

    fun <T : Any> query(sql: Fragment, codec: RowCodec<T>): List<T> =
        underlying.query(sql.underlying, codec.underlying)

    fun <T : Any> queryFirst(sql: Fragment, codec: RowCodec<T>): T? =
        underlying.queryFirst(sql.underlying, codec.underlying).orElse(null)

    fun update(sql: Fragment): Int =
        underlying.update(sql.underlying)

    fun rollbackOnly(): Transactor =
        Transactor(underlying.rollbackOnly())

    fun withListener(listener: QueryListener): Transactor =
        Transactor(underlying.withListener(listener))

    fun mergeListener(listener: QueryListener): Transactor =
        Transactor(underlying.mergeListener(listener))

    override fun close() = underlying.close()

    companion object {
        /**
         * Create a JDBC-backed transactor. The underlying Java transactor implements
         * [dev.typr.foundations.TransactorJdbc] for raw JDBC access via `underlying`.
         */
        @JvmStatic
        fun create(config: dev.typr.foundationskt.connect.DatabaseConfig): Transactor =
            Transactor(dev.typr.foundations.Transactor.create(config))

        /**
         * Create a JDBC-backed transactor. The underlying Java transactor implements
         * [dev.typr.foundations.TransactorJdbc] for raw JDBC access via `underlying`.
         */
        @JvmStatic
        fun create(config: dev.typr.foundationskt.connect.DatabaseConfig, settings: dev.typr.foundationskt.connect.ConnectionSettings): Transactor =
            Transactor(dev.typr.foundations.Transactor.create(config, settings))

    }
}
