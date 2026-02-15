package dev.typr.scalafoundations

import java.sql.Connection

class SingleConnectionDataSource private (
    private val underlying: dev.typr.foundations.connect.SingleConnectionDataSource
) extends ConnectionSource:

  override def getConnection(): Connection = underlying.getConnection()

  override def transactor(strategy: Transactor.Strategy): Transactor =
    Transactor(underlying.transactor(strategy))

object SingleConnectionDataSource:
  def create(config: dev.typr.foundations.connect.DatabaseConfig): SingleConnectionDataSource =
    new SingleConnectionDataSource(dev.typr.foundations.connect.SingleConnectionDataSource.create(config))

  def create(config: dev.typr.foundations.connect.DatabaseConfig, settings: dev.typr.foundations.connect.ConnectionSettings): SingleConnectionDataSource =
    new SingleConnectionDataSource(dev.typr.foundations.connect.SingleConnectionDataSource.create(config, settings))
