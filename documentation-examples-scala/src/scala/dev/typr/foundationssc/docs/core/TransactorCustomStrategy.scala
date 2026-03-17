package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object TransactorCustomStrategy:
  // start
  val customStrategy: Transactor.Strategy =
    Strategy
      .empty()
      .replaceOnBegin(conn => conn.setAutoCommit(false))
      .replaceOnSuccess((conn: Connection) => conn.commit())
      .replaceOnFailure((_, _) => { /* handle error */ })
      .replaceOnComplete((conn: Connection) => conn.close())
  // stop
