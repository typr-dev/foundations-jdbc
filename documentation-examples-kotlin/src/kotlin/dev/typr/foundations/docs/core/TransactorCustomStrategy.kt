package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.Connection

@Suppress("unused")
class TransactorCustomStrategy {
    //start
    val customStrategy: Strategy =
        Strategy.empty()
            .replaceBefore { conn -> conn.autoCommit = false }
            .replaceAfter(Connection::commit)
            .replaceOops { _, _ -> /* handle error */ }
            .replaceAlways(Connection::close)
    //stop
}
