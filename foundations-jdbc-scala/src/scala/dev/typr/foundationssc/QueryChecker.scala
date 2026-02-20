package dev.typr.foundationssc

import scala.jdk.CollectionConverters.*

class QueryChecker(val underlying: dev.typr.foundations.QueryChecker):
  def check(analyzable: Analyzable): Unit =
    underlying.check(analyzable.analyzable)
  def checkAll(analyzables: List[Analyzable]): Unit =
    underlying.checkAll(analyzables.map(_.analyzable).asJava)
  def checkAll(analyzables: Analyzable*): Unit =
    checkAll(analyzables.toList)
  def checkRoutine(procedure: dev.typr.foundations.Procedure[?]): Unit =
    underlying.checkRoutine(procedure)

object QueryChecker:
  def create(tx: Transactor): QueryChecker =
    new QueryChecker(new dev.typr.foundations.QueryChecker:
      def transactor(): dev.typr.foundations.Transactor = tx.underlying
    )
