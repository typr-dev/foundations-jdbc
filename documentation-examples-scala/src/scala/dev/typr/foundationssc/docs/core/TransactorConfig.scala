package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.connect.*

import dev.typr.foundations.QueryListener

@SuppressWarnings(Array("unused"))
object TransactorCustomStrategy:
  val config: PgConfig = null // placeholder
  val myListener: QueryListener = QueryListener.NOOP // placeholder
  // start
  val tx: Transactor = Transactor.create(config)

  val testTx: Transactor = tx.rollbackOnly()

  val txWithListener: Transactor = tx.withListener(myListener)
  // stop
