package dev.typr.foundationssc

import scala.jdk.CollectionConverters.*

class QueryChecker(val underlying: dev.typr.foundations.QueryChecker):
  def check(analyzable: Analyzable): Unit =
    underlying.check(analyzable.analyzable)
  def analyzeAll(analyzables: List[Analyzable]): CheckReport =
    underlying.analyzeAll(analyzables.map(_.analyzable).asJava)
  def analyzeAll(analyzables: Analyzable*): CheckReport =
    analyzeAll(analyzables.toList)
  def checkAll(analyzables: List[Analyzable]): CheckReport =
    underlying.checkAll(analyzables.map(_.analyzable).asJava)
  def checkAll(analyzables: Analyzable*): CheckReport =
    checkAll(analyzables.toList)
  def checkRoutine(definition: RoutineDef): Unit =
    underlying.checkRoutine(definition)

object QueryChecker:
  def create(tx: Transactor): QueryChecker =
    new QueryChecker(
      new dev.typr.foundations.QueryChecker:
        def transactor(): dev.typr.foundations.Transactor = tx.underlying
    )
