package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*

import dev.typr.foundations.QueryListener

@SuppressWarnings(Array("unused"))
object StrategyMerge:
  val tx: Transactor = null // placeholder
  val logger: QueryListener = QueryListener.NOOP
  // start
  val txWithLogging: Transactor = tx.mergeListener(logger)
  // stop
