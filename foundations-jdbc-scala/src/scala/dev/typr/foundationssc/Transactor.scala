package dev.typr.foundationssc

import java.sql.Connection

class Transactor(val underlying: dev.typr.foundations.Transactor):
  def execute[T](operation: Operation[T]): T =
    val f: dev.typr.foundations.SqlFunction[Connection, T] = (conn: Connection) => operation.run(conn)
    underlying.execute(f)

  def transact[T](f: Connection => T): T =
    val sf: dev.typr.foundations.SqlFunction[Connection, T] = (conn: Connection) => f(conn)
    underlying.execute(sf)

  def executeVoid(f: Connection => Unit): Unit =
    underlying.executeVoid((conn: Connection) => f(conn))

  def withStrategy(override_ : Transactor.Strategy): Transactor =
    new Transactor(underlying.withStrategy(override_))

  def mergeListener(listener: dev.typr.foundations.QueryListener): Transactor =
    new Transactor(underlying.mergeListener(listener))

  def transact[T](override_ : Transactor.Strategy)(f: Connection => T): T =
    withStrategy(override_).transact(f)

object Transactor:
  def apply(underlying: dev.typr.foundations.Transactor): Transactor = new Transactor(underlying)
  type Strategy = dev.typr.foundations.Transactor.Strategy

  def create(config: connect.DatabaseConfig): Transactor =
    new Transactor(dev.typr.foundations.Transactor.create(config))
  def create(config: connect.DatabaseConfig, strategy: Strategy): Transactor =
    new Transactor(dev.typr.foundations.Transactor.create(config, strategy))
  def create(config: connect.DatabaseConfig, settings: connect.ConnectionSettings): Transactor =
    new Transactor(dev.typr.foundations.Transactor.create(config, settings))
  def create(config: connect.DatabaseConfig, settings: connect.ConnectionSettings, strategy: Strategy): Transactor =
    new Transactor(dev.typr.foundations.Transactor.create(config, settings, strategy))

  /** Create a Transactor from an existing [[ConnectionSource]] (e.g. from
    * [[ConnectionSource.of]] or a custom pool). */
  def create(source: ConnectionSource): Transactor = source.transactor()
  def create(source: ConnectionSource, strategy: Strategy): Transactor = source.transactor(strategy)

  def defaultStrategy(): Strategy = dev.typr.foundations.Transactor.defaultStrategy()
  def autoCommitStrategy(): Strategy = dev.typr.foundations.Transactor.autoCommitStrategy()
  def testStrategy(): Strategy = dev.typr.foundations.Transactor.testStrategy()
