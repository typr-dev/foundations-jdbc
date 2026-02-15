package dev.typr.scalafoundations

import java.sql.Connection

trait ConnectionSource:
  def getConnection(): Connection
  def transactor(): Transactor = transactor(Transactor.defaultStrategy())
  def transactor(strategy: Transactor.Strategy): Transactor
