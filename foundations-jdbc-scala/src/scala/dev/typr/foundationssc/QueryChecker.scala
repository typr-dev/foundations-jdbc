package dev.typr.scalafoundations

type QueryChecker = dev.typr.foundations.QueryChecker

object QueryChecker:
  def create(tx: Transactor): dev.typr.foundations.QueryChecker =
    new dev.typr.foundations.QueryChecker:
      def transactor(): dev.typr.foundations.Transactor = tx.underlying
  def checkRoutine(checker: dev.typr.foundations.QueryChecker, procedure: Procedure[?]): Unit =
    checker.checkRoutine(procedure.javaProcedure)
