package dev.typr.foundationssc

class DuckDbType[T](override val underlying: dev.typr.foundations.DuckDbType[T]) extends DbType[T](underlying):
  override def opt: DuckDbType[Option[T]] =
    DuckDbType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: dev.typr.foundations.Bijection[T, B]): DuckDbType[B] =
    DuckDbType(underlying.to(bijection))

  def transform[B](f: T => B, g: B => T): DuckDbType[B] =
    DuckDbType(underlying.transform(v => f(v), v => g(v)))

  def mapTo[V](valueType: DuckDbType[V]): DuckDbType[Map[T, V]] = DuckDbType(underlying.mapTo(valueType.underlying).transform(
    jmap => scala.jdk.CollectionConverters.MapHasAsScala(jmap).asScala.toMap,
    smap => java.util.Map.copyOf(scala.jdk.CollectionConverters.MapHasAsJava(smap).asJava)
  ))

  def list: DuckDbType[List[T]] = DuckDbType(underlying.list().transform(
    jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
    slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
  ))

  def unchecked(): DuckDbType[T] = DuckDbType(underlying.unchecked())
  def nullableOk(): DuckDbType[T] = DuckDbType(underlying.nullableOk())
