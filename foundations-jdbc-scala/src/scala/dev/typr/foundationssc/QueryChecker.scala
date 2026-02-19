package dev.typr.foundationssc

type QueryChecker = dev.typr.foundations.QueryChecker

object QueryChecker:
  def create(tx: Transactor): dev.typr.foundations.QueryChecker =
    new dev.typr.foundations.QueryChecker:
      def transactor(): dev.typr.foundations.Transactor = tx.underlying
  def checkRoutine(checker: dev.typr.foundations.QueryChecker, procedure: Procedure[?]): Unit =
    checker.checkRoutine(procedure.javaProcedure)

extension (checker: dev.typr.foundations.QueryChecker)
  def check(op: Operation[?]): Unit = checker.check(op.underlying)
  def check(template: SqlTemplate[?, ?]): Unit = checker.check(template.underlying)
