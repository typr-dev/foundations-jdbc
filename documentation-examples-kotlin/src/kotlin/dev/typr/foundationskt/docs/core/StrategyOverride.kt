package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*

@Suppress("unused")
class StrategyOverride {
    val config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build()
    val logger: QueryListener = QueryListener.NOOP

    //start
    val tx: Transactor = Transactor.create(config)
    val txWithLogging: Transactor = tx.mergeListener(logger)
    //stop
}
