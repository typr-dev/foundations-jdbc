package dev.typr.scalafoundations

import java.sql.Connection

class SimpleDataSource private (
    private val underlying: dev.typr.foundations.connect.SimpleDataSource
) extends ConnectionSource:

  override def getConnection(): Connection = underlying.getConnection()

  override def transactor(strategy: Transactor.Strategy): Transactor =
    Transactor(underlying.transactor(strategy))

  def config(): dev.typr.foundations.connect.DatabaseConfig = underlying.config()

  def settings(): dev.typr.foundations.connect.ConnectionSettings = underlying.settings()

object SimpleDataSource:
  def create(config: dev.typr.foundations.connect.DatabaseConfig): SimpleDataSource =
    new SimpleDataSource(dev.typr.foundations.connect.SimpleDataSource.create(config))

  def create(config: dev.typr.foundations.connect.DatabaseConfig, settings: dev.typr.foundations.connect.ConnectionSettings): SimpleDataSource =
    new SimpleDataSource(dev.typr.foundations.connect.SimpleDataSource.create(config, settings))
