package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.Connection

@Suppress("unused")
class TransactorCustomStrategy {
    //start
    val customStrategy: Strategy = Strategy(
        { conn -> conn.autoCommit = false },  // before
        Connection::commit,                    // after (success)
        { _ -> /* handle error */ },           // oops
        Connection::close                      // always (finally)
    )
    //stop
}
