package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import dev.typr.foundations.{QueryListener, QueryEvent}

@SuppressWarnings(Array("unused"))
object InterpolatedSql:
  // start
  object debugListener extends QueryListener:
    override def beforeQuery(sql: String, name: java.util.Optional[String]): Unit = ()
    override def afterQuery(event: QueryEvent): Unit =
      println(event.interpolatedSql())
    override def failedQuery(event: QueryEvent): Unit =
      System.err.println(s"Failed: ${event.interpolatedSql()}")
  // stop
