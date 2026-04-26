package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import dev.typr.foundations.{QueryListener, QueryEvent}

@SuppressWarnings(Array("unused"))
object QueryListenerBasic:
  // start
  object logger extends QueryListener:
    override def beforeQuery(sql: String, name: java.util.Optional[String]): Unit =
      println(s"Executing: $sql")
    override def afterQuery(event: QueryEvent): Unit =
      println(s"${event.name().orElse("unnamed")} completed in ${event.elapsed().toMillis}ms")
    override def failedQuery(event: QueryEvent): Unit =
      System.err.println(s"${event.name().orElse("unnamed")} failed after ${event.elapsed().toMillis}ms: ${event.error().map(_.getMessage).orElse("unknown")}")
  // stop
