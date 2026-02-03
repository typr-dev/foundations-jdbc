package dev.typr.foundations.docs.core

import dev.typr.foundations.Transactor
import java.sql.Connection

@Suppress("unused")
class TransactorCustomStrategy {
    //start
    val customStrategy: Transactor.Strategy = Transactor.Strategy(
        { conn -> conn.autoCommit = false },  // before
        Connection::commit,                    // after (success)
        { _ -> /* handle error */ },           // oops
        Connection::close                      // always (finally)
    )
    //stop
}
