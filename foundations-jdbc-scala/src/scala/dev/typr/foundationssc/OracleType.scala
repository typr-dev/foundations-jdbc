package dev.typr.foundationssc

class OracleType[T](override val underlying: dev.typr.foundations.OracleType[T]) extends DbType[T](underlying):
  override def opt: OracleType[Option[T]] =
    OracleType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: dev.typr.foundations.Bijection[T, B]): OracleType[B] =
    OracleType(underlying.to(bijection))

  def transform[B](f: dev.typr.foundations.SqlFunction[T, B], g: java.util.function.Function[B, T]): OracleType[B] =
    OracleType(underlying.transform(f, g))

  def unchecked(): OracleType[T] = OracleType(underlying.unchecked())
  def nullableOk(): OracleType[T] = OracleType(underlying.nullableOk())
