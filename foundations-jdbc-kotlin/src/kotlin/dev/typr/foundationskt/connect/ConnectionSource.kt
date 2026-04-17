package dev.typr.foundationskt.connect

import dev.typr.foundationskt.Transactor
import java.sql.Connection

interface ConnectionSource {
    fun getConnection(): Connection
    fun transactor(): Transactor = transactor(Transactor.defaultStrategy())
    fun transactor(strategy: dev.typr.foundations.Transactor.Strategy): Transactor

    companion object {
        @JvmStatic
        fun of(config: DatabaseConfig): ConnectionSource {
            val java = dev.typr.foundations.connect.ConnectionSource.of(config)
            return wrap(java)
        }

        @JvmStatic
        fun of(config: DatabaseConfig, settings: ConnectionSettings): ConnectionSource {
            val java = dev.typr.foundations.connect.ConnectionSource.of(config, settings)
            return wrap(java)
        }

        internal fun wrap(java: dev.typr.foundations.connect.ConnectionSource): ConnectionSource =
            object : ConnectionSource {
                override fun getConnection(): Connection = java.getConnection()
                override fun transactor(strategy: dev.typr.foundations.Transactor.Strategy): Transactor =
                    Transactor(java.transactor(strategy))
            }
    }
}
