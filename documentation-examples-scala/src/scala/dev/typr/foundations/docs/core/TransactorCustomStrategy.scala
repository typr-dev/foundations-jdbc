package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.sql.Connection

@SuppressWarnings(Array("unused"))
object TransactorCustomStrategy:
  //start
  val customStrategy: Transactor.Strategy = new Transactor.Strategy(
    conn => conn.setAutoCommit(false), // before
    (conn: Connection) => conn.commit(), // after (success)
    (throwable: Throwable) => { /* handle error */ }, // oops
    (conn: Connection) => conn.close() // always (finally)
  )
  //stop
