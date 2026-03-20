package dev.typr.foundationssc

class SqlServerType[T](override val underlying: dev.typr.foundations.SqlServerType[T]) extends DbType[T](underlying):
  override def opt: SqlServerType[Option[T]] =
    SqlServerType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: dev.typr.foundations.Bijection[T, B]): SqlServerType[B] =
    SqlServerType(underlying.to(bijection))

  def transform[B](f: T => B, g: B => T): SqlServerType[B] =
    SqlServerType(underlying.transform(v => f(v), v => g(v)))

  def unchecked(): SqlServerType[T] = SqlServerType(underlying.unchecked())
  def nullableOk(): SqlServerType[T] = SqlServerType(underlying.nullableOk())
