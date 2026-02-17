package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import dev.typr.foundations.QueryListener
import java.sql.Connection

@SuppressWarnings(Array("unused"))
object TransactorCustomStrategy:
  //start
  val customStrategy: Transactor.Strategy = new Transactor.Strategy(
    conn => conn.setAutoCommit(false), // before
    (conn: Connection) => conn.commit(), // after (success)
    (conn: Connection, t: Throwable) => { /* handle error */ }, // oops
    (conn: Connection) => conn.close(), // always (finally)
    QueryListener.NOOP // listener
  )
  //stop
