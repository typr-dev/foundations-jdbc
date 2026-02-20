package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object TransactorCustomStrategy:
  //start
  val customStrategy: Transactor.Strategy =
    Strategy.empty()
      .replaceBefore(conn => conn.setAutoCommit(false))
      .replaceAfter((conn: Connection) => conn.commit())
      .replaceOops((_, _) => { /* handle error */ })
      .replaceAlways((conn: Connection) => conn.close())
  //stop
