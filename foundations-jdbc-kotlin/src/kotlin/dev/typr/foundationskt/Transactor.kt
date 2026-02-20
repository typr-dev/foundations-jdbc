@file:Suppress("unused")
package dev.typr.foundationskt

import java.sql.Connection

typealias Strategy = dev.typr.foundations.Transactor.Strategy

class Transactor(val underlying: dev.typr.foundations.Transactor) {

    constructor(connect: dev.typr.foundations.SqlSupplier<Connection>, strategy: Strategy) :
        this(dev.typr.foundations.Transactor(connect, strategy))

    fun <T> execute(operation: Operation<T>): T {
        @Suppress("UNCHECKED_CAST")
        return underlying.execute(operation.underlying as dev.typr.foundations.Operation<T>)
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
        fun defaultStrategy(): Strategy = dev.typr.foundations.Transactor.defaultStrategy()

        @JvmStatic
        fun autoCommitStrategy(): Strategy = dev.typr.foundations.Transactor.autoCommitStrategy()

        @JvmStatic
        fun rollbackOnErrorStrategy(): Strategy = dev.typr.foundations.Transactor.rollbackOnErrorStrategy()

        @JvmStatic
        fun testStrategy(): Strategy = dev.typr.foundations.Transactor.testStrategy()
    }
}
