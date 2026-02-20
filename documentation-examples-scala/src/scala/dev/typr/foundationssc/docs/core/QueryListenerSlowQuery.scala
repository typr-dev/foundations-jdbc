package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import dev.typr.foundations.{QueryListener, QueryEvent}
import java.time.Duration

@SuppressWarnings(Array("unused"))
object QueryListenerSlowQuery:
  //start
  def slowQueryDetector(threshold: Duration): QueryListener =
    new QueryListener:
      override def beforeQuery(sql: String, name: String): Unit = ()
      override def afterQuery(event: QueryEvent): Unit =
        if event.elapsed().compareTo(threshold) > 0 then
          System.err.println(s"SLOW QUERY [${event.elapsed().toMillis}ms]: ${event.interpolatedSql()}")
      override def failedQuery(event: QueryEvent): Unit = ()
  //stop
