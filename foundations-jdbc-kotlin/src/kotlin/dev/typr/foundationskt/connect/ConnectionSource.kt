package dev.typr.foundationskt.connect

import dev.typr.foundationskt.Transactor
import java.sql.Connection

interface ConnectionSource {
    fun getConnection(): Connection
    fun transactor(): Transactor = transactor(Transactor.defaultStrategy())
    fun transactor(strategy: dev.typr.foundations.Transactor.Strategy): Transactor
}
