package dev.typr.scalafoundations

class MariaType[T](override val underlying: dev.typr.foundations.MariaType[T]) extends DbType[T](underlying):
  override def opt: MariaType[Option[T]] =
    MariaType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: dev.typr.foundations.Bijection[T, B]): MariaType[B] =
    MariaType(underlying.to(bijection))

  def bimap[B](f: dev.typr.foundations.SqlFunction[T, B], g: java.util.function.Function[B, T]): MariaType[B] =
    MariaType(underlying.bimap(f, g))

  def unchecked(): MariaType[T] = MariaType(underlying.unchecked())
  def nullableOk(): MariaType[T] = MariaType(underlying.nullableOk())
