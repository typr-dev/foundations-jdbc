package dev.typr.foundationssc

import java.sql.Connection

trait ConnectionSource:
  def getConnection(): Connection
  def transactor(): Transactor = transactor(Transactor.defaultStrategy())
  def transactor(strategy: Transactor.Strategy): Transactor
