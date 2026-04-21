package dev.typr.foundationssc

class MariaType[T](override val underlying: dev.typr.foundations.MariaType[T]) extends DbType[T](underlying):
  override def opt: MariaType[Option[T]] =
    MariaType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: Bijection[T, B]): MariaType[B] =
    MariaType(underlying.to(bijection))

  def transform[B](f: T => B, g: B => T): MariaType[B] =
    MariaType(underlying.transform(v => f(v), v => g(v)))

  def unchecked(): MariaType[T] = MariaType(underlying.unchecked())
  def nullableOk(): MariaType[T] = MariaType(underlying.nullableOk())
