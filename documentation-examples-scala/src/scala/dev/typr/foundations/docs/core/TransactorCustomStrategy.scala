package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object TransactorCustomStrategy:
  //start
  val customStrategy: Transactor.Strategy =
    Strategy.empty()
      .withBefore(conn => conn.setAutoCommit(false))
      .withAfter((conn: Connection) => conn.commit())
      .withOops((_, _) => { /* handle error */ })
      .withAlways((conn: Connection) => conn.close())
  //stop
