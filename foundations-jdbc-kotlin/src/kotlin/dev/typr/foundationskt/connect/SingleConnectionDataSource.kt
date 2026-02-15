package dev.typr.kotlinfoundations.connect

import dev.typr.kotlinfoundations.Transactor
import java.sql.Connection

class SingleConnectionDataSource private constructor(
    private val underlying: dev.typr.foundations.connect.SingleConnectionDataSource
) : ConnectionSource {

    override fun getConnection(): Connection = underlying.getConnection()

    override fun transactor(strategy: dev.typr.foundations.Transactor.Strategy): Transactor =
        Transactor(underlying.transactor(strategy))

    companion object {
        @JvmStatic
        fun create(config: DatabaseConfig): SingleConnectionDataSource =
            SingleConnectionDataSource(dev.typr.foundations.connect.SingleConnectionDataSource.create(config))

        @JvmStatic
        fun create(config: DatabaseConfig, settings: ConnectionSettings): SingleConnectionDataSource =
            SingleConnectionDataSource(dev.typr.foundations.connect.SingleConnectionDataSource.create(config, settings))
    }
}
