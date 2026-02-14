package dev.typr.scalafoundations

class SqlServerType[T](override val underlying: dev.typr.foundations.SqlServerType[T]) extends DbType[T](underlying):
  override def opt: SqlServerType[Option[T]] =
    SqlServerType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: dev.typr.foundations.Bijection[T, B]): SqlServerType[B] =
    SqlServerType(underlying.to(bijection))

  def bimap[B](f: dev.typr.foundations.SqlFunction[T, B], g: java.util.function.Function[B, T]): SqlServerType[B] =
    SqlServerType(underlying.bimap(f, g))

  def unchecked(): SqlServerType[T] = SqlServerType(underlying.unchecked())
  def nullableOk(): SqlServerType[T] = SqlServerType(underlying.nullableOk())
