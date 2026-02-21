package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.Connection

@Suppress("unused")
class TransactorCustomStrategy {
    //start
    val customStrategy: Strategy =
        Strategy.empty()
            .replaceOnBegin { conn -> conn.autoCommit = false }
            .replaceOnSuccess(Connection::commit)
            .replaceOnFailure { _, _ -> /* handle error */ }
            .replaceOnComplete(Connection::close)
    //stop
}
