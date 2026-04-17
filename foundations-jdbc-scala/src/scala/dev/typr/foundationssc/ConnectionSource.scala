package dev.typr.foundationssc

import java.sql.Connection

trait ConnectionSource:
  def getConnection(): Connection
  def transactor(): Transactor = transactor(Transactor.defaultStrategy())
  def transactor(strategy: Transactor.Strategy): Transactor

object ConnectionSource:
  def of(config: connect.DatabaseConfig): ConnectionSource =
    val java = dev.typr.foundations.connect.ConnectionSource.of(config)
    wrap(java)

  def of(config: connect.DatabaseConfig, settings: connect.ConnectionSettings): ConnectionSource =
    val java = dev.typr.foundations.connect.ConnectionSource.of(config, settings)
    wrap(java)

  private[foundationssc] def wrap(java: dev.typr.foundations.connect.ConnectionSource): ConnectionSource =
    new ConnectionSource:
      override def getConnection(): Connection = java.getConnection()
      override def transactor(strategy: Transactor.Strategy): Transactor =
        Transactor(java.transactor(strategy))
