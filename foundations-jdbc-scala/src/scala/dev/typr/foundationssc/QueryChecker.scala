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
  def checkRoutine(procedure: Procedure[?]): Unit =
    underlying.checkRoutine(procedure)

object QueryChecker:
  def create(tx: Transactor): QueryChecker =
    create(tx, 1)

  def create(tx: Transactor, threads: Int): QueryChecker =
    new QueryChecker(
      dev.typr.foundations.QueryChecker.create(tx.underlying, threads)
    )
