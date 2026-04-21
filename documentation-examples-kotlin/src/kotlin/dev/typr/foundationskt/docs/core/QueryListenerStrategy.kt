package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*

@Suppress("unused")
class QueryListenerStrategy {
    val config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build()
    val logger: QueryListener = QueryListener.NOOP

    //start
    val tx: Transactor =
        Transactor.create(config)
            .withListener(logger)
    //stop
}
