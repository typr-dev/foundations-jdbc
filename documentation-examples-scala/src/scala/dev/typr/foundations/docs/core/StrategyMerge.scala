package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import dev.typr.foundations.{QueryListener, QueryEvent}
import java.sql.Connection

@SuppressWarnings(Array("unused"))
object StrategyMerge:
  val logger: QueryListener = QueryListener.NOOP
  //start
  val base: Transactor.Strategy = Transactor.defaultStrategy()
  val withLogging: Transactor.Strategy = base.merge(
    new Transactor.Strategy(
      (_: Connection) => (), (_: Connection) => (),
      (_: Connection, _: Throwable) => (), (_: Connection) => (),
      logger))
  //stop
