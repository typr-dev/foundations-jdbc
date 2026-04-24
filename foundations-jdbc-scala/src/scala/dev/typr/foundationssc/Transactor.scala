package dev.typr.foundationssc

import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*

class Transactor(val underlying: dev.typr.foundations.Transactor) extends AutoCloseable:
  def execute[T](operation: OperationRead[T]): T =
    underlying.execute(operation.underlying)

  def execute[T](operation: Operation[T]): T =
    underlying.execute(operation.underlying)

  def transact[T](f: Connection ?=> T): T =
    underlying.transact(mc => f(using mc))

  def transactRead[T](f: ConnectionRead ?=> T): T =
    underlying.transactRead(rc => f(using rc))

  def query[T](sql: Fragment, codec: RowCodec[T]): List[T] =
    underlying.query(sql.underlying, codec.underlying).asScala.toList

  def queryFirst[T](sql: Fragment, codec: RowCodec[T]): Option[T] =
    underlying.queryFirst(sql.underlying, codec.underlying).toScala

  def update(sql: Fragment): Int =
    underlying.update(sql.underlying)

  def rollbackOnly(): Transactor =
    new Transactor(underlying.rollbackOnly())

  def withListener(listener: QueryListener): Transactor =
    new Transactor(underlying.withListener(listener))

  def mergeListener(listener: QueryListener): Transactor =
    new Transactor(underlying.mergeListener(listener))

  override def close(): Unit = underlying.close()

object Transactor:
  def apply(underlying: dev.typr.foundations.Transactor): Transactor = new Transactor(underlying)

  def create(config: connect.DatabaseConfig): Transactor =
    new Transactor(dev.typr.foundations.Transactor.create(config))
  def create(config: connect.DatabaseConfig, settings: connect.ConnectionSettings): Transactor =
    new Transactor(dev.typr.foundations.Transactor.create(config, settings))
