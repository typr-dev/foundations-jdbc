package dev.typr.foundationssc

class PgType[T](override val underlying: dev.typr.foundations.PgType[T]) extends DbType[T](underlying):
  override def opt: PgType[Option[T]] =
    PgType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: dev.typr.foundations.Bijection[T, B]): PgType[B] =
    PgType(underlying.to(bijection))

  def transform[B](f: T => B, g: B => T): PgType[B] =
    PgType(underlying.transform(v => f(v), v => g(v)))

  def array(): PgType[Array[T]] = PgType(underlying.array().asInstanceOf[dev.typr.foundations.PgType[Array[T]]])

  def pgText(): dev.typr.foundations.PgText[T] = underlying.pgText()

  def unchecked(): PgType[T] = PgType(underlying.unchecked())
  def nullableOk(): PgType[T] = PgType(underlying.nullableOk())
