package dev.typr.foundationssc

class Db2Type[T](override val underlying: dev.typr.foundations.Db2Type[T]) extends DbType[T](underlying):
  override def opt: Db2Type[Option[T]] =
    Db2Type(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: dev.typr.foundations.Bijection[T, B]): Db2Type[B] =
    Db2Type(underlying.to(bijection))

  def bimap[B](f: dev.typr.foundations.SqlFunction[T, B], g: java.util.function.Function[B, T]): Db2Type[B] =
    Db2Type(underlying.bimap(f, g))

  def unchecked(): Db2Type[T] = Db2Type(underlying.unchecked())
  def nullableOk(): Db2Type[T] = Db2Type(underlying.nullableOk())
