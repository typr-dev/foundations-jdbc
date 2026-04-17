package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class GettingStarted {
    //start
    val tx: Transactor =
        ConnectionSource.of(DuckDbConfig.inMemory().build())
            .transactor()

    val result: Int = sql { "SELECT 42" }
        .queryExactlyOne(DuckDbTypes.integer)
        .transact(tx)
    //stop
}
