package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import dev.typr.foundations.{QueryListener, QueryEvent}
import java.sql.Connection

@SuppressWarnings(Array("unused"))
object QueryListenerBasic:
  //start
  object logger extends QueryListener:
    override def beforeQuery(sql: String, name: String): Unit =
      println(s"Executing: $sql")
    override def afterQuery(event: QueryEvent): Unit =
      println(s"${event.name()} completed in ${event.elapsed().toMillis}ms")
    override def failedQuery(event: QueryEvent): Unit =
      System.err.println(s"${event.name()} failed after ${event.elapsed().toMillis}ms: ${event.error().getMessage}")
  //stop
