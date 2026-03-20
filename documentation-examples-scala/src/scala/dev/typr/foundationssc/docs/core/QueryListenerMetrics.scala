package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import dev.typr.foundations.{QueryListener, QueryEvent}

@SuppressWarnings(Array("unused"))
object QueryListenerMetrics:
  // start
  def metricsListener( /* registry: MeterRegistry */ ): QueryListener =
    new QueryListener:
      override def beforeQuery(sql: String, name: String): Unit = ()
      override def afterQuery(event: QueryEvent): Unit = ()
      // registry.timer("db.query",
      //   "name", event.name(),
      //   "outcome", "success"
      // ).record(event.elapsed())
      override def failedQuery(event: QueryEvent): Unit = ()
      // registry.timer("db.query",
      //   "name", event.name(),
      //   "outcome", "failure"
      // ).record(event.elapsed())
  // stop
