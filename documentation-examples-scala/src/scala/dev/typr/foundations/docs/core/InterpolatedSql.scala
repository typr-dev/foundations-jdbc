package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import dev.typr.foundations.{QueryListener, QueryEvent}

@SuppressWarnings(Array("unused"))
object InterpolatedSql:
  //start
  val debugListener: QueryListener = new QueryListener:
    override def beforeQuery(sql: String, name: String): Unit = ()
    override def afterQuery(event: QueryEvent): Unit =
      println(event.interpolatedSql())
    override def failedQuery(event: QueryEvent): Unit =
      System.err.println(s"Failed: ${event.interpolatedSql()}")
  //stop
