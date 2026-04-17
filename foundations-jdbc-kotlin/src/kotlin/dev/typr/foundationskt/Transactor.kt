@file:Suppress("unused")
package dev.typr.foundationskt

import java.sql.Connection

typealias Strategy = dev.typr.foundations.Transactor.Strategy

class Transactor(val underlying: dev.typr.foundations.Transactor) {

    constructor(connect: dev.typr.foundations.SqlSupplier<Connection>, strategy: Strategy) :
        this(dev.typr.foundations.Transactor(connect, strategy))

    fun <T> execute(operation: Operation<T>): T {
        return underlying.execute(dev.typr.foundations.SqlFunction { conn -> operation.run(conn) })
    }

    fun <T> transact(f: (Connection) -> T): T =
        underlying.execute(dev.typr.foundations.SqlFunction(f))

    fun executeVoid(f: (Connection) -> Unit) {
        underlying.executeVoid { conn -> f(conn) }
    }

    fun withStrategy(override: Strategy): Transactor =
        Transactor(underlying.withStrategy(override))

    fun mergeListener(listener: QueryListener): Transactor =
        Transactor(underlying.mergeListener(listener))

    fun <T> transact(override: Strategy, f: (Connection) -> T): T =
        withStrategy(override).transact(f)

    companion object {
        @JvmStatic
        fun create(config: dev.typr.foundationskt.connect.DatabaseConfig): Transactor =
            Transactor(dev.typr.foundations.Transactor.create(config))

        @JvmStatic
        fun create(config: dev.typr.foundationskt.connect.DatabaseConfig, strategy: Strategy): Transactor =
            Transactor(dev.typr.foundations.Transactor.create(config, strategy))

        @JvmStatic
        fun create(config: dev.typr.foundationskt.connect.DatabaseConfig, settings: dev.typr.foundationskt.connect.ConnectionSettings): Transactor =
            Transactor(dev.typr.foundations.Transactor.create(config, settings))

        @JvmStatic
        fun create(config: dev.typr.foundationskt.connect.DatabaseConfig, settings: dev.typr.foundationskt.connect.ConnectionSettings, strategy: Strategy): Transactor =
            Transactor(dev.typr.foundations.Transactor.create(config, settings, strategy))

        /**
         * Create a Transactor directly from an existing [dev.typr.foundationskt.connect.ConnectionSource].
         * Useful when you already hold a [dev.typr.foundationskt.connect.ConnectionSource] from
         * [dev.typr.foundationskt.connect.ConnectionSource.of] or a custom pool.
         */
        @JvmStatic
        fun create(source: dev.typr.foundationskt.connect.ConnectionSource, strategy: Strategy): Transactor =
            source.transactor(strategy)

        @JvmStatic
        fun create(source: dev.typr.foundationskt.connect.ConnectionSource): Transactor =
            source.transactor()

        @JvmStatic
        fun defaultStrategy(): Strategy = dev.typr.foundations.Transactor.defaultStrategy()

        @JvmStatic
        fun autoCommitStrategy(): Strategy = dev.typr.foundations.Transactor.autoCommitStrategy()

        @JvmStatic
        fun testStrategy(): Strategy = dev.typr.foundations.Transactor.testStrategy()
    }
}
