package dev.typr.kotlinfoundations.connect

import dev.typr.kotlinfoundations.Transactor
import java.sql.Connection

interface ConnectionSource {
    fun getConnection(): Connection
    fun transactor(): Transactor = transactor(Transactor.defaultStrategy())
    fun transactor(strategy: dev.typr.foundations.Transactor.Strategy): Transactor
}
