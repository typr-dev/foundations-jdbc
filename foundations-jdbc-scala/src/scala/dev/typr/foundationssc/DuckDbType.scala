package dev.typr.scalafoundations

class DuckDbType[T](override val underlying: dev.typr.foundations.DuckDbType[T]) extends DbType[T](underlying):
  override def opt: DuckDbType[Option[T]] =
    DuckDbType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: dev.typr.foundations.Bijection[T, B]): DuckDbType[B] =
    DuckDbType(underlying.to(bijection))

  def bimap[B](f: dev.typr.foundations.SqlFunction[T, B], g: java.util.function.Function[B, T]): DuckDbType[B] =
    DuckDbType(underlying.bimap(f, g))

  def mapTo[V](valueType: DuckDbType[V]): DuckDbType[java.util.Map[T, V]] =
    DuckDbType(underlying.mapTo(valueType.underlying))

  def unchecked(): DuckDbType[T] = DuckDbType(underlying.unchecked())
  def nullableOk(): DuckDbType[T] = DuckDbType(underlying.nullableOk())
