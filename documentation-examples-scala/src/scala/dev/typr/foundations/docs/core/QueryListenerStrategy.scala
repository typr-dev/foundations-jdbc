package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import dev.typr.foundations.{QueryListener, QueryEvent}

@SuppressWarnings(Array("unused"))
object QueryListenerStrategy:
  val logger: QueryListener = QueryListener.NOOP
  //start
  val strategy: Transactor.Strategy =
    Transactor.defaultStrategy()
      .withListener(logger)
  //stop
