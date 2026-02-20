package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.Connection

@Suppress("unused")
class TransactorCustomStrategy {
    //start
    val customStrategy: Strategy =
        Strategy.empty()
            .withBefore { conn -> conn.autoCommit = false }
            .withAfter(Connection::commit)
            .withOops { _, _ -> /* handle error */ }
            .withAlways(Connection::close)
    //stop
}
