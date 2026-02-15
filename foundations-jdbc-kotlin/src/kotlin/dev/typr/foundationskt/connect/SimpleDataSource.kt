package dev.typr.kotlinfoundations.connect

import dev.typr.kotlinfoundations.Transactor
import java.sql.Connection

class SimpleDataSource private constructor(
    private val underlying: dev.typr.foundations.connect.SimpleDataSource
) : ConnectionSource {

    override fun getConnection(): Connection = underlying.getConnection()

    override fun transactor(strategy: dev.typr.foundations.Transactor.Strategy): Transactor =
        Transactor(underlying.transactor(strategy))

    fun config(): DatabaseConfig = underlying.config()

    fun settings(): ConnectionSettings = underlying.settings()

    companion object {
        @JvmStatic
        fun create(config: DatabaseConfig): SimpleDataSource =
            SimpleDataSource(dev.typr.foundations.connect.SimpleDataSource.create(config))

        @JvmStatic
        fun create(config: DatabaseConfig, settings: ConnectionSettings): SimpleDataSource =
            SimpleDataSource(dev.typr.foundations.connect.SimpleDataSource.create(config, settings))
    }
}
