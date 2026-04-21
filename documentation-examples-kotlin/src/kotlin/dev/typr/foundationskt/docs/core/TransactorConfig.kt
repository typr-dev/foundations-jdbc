package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*

@Suppress("unused")
class TransactorCustomStrategy {
    val config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build()

    //start
    val tx: Transactor = Transactor.create(config)

    val testTx: Transactor = tx.rollbackOnly()

    val txWithListener: Transactor = tx.withListener(QueryListener.NOOP)
    //stop
}
