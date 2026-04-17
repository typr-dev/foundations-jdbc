package dev.typr.foundationssc

open class DbType[T](val underlying: dev.typr.foundations.DbType[T]):
  def opt: DbType[Option[T]] =
    DbType(underlying.opt().to(Bijections.optionalToOption))

  def to[B](bijection: dev.typr.foundations.Bijection[T, B]): DbType[B] =
    DbType(underlying.to(bijection))

  /** Whether this type allows null values. True for types created with [[opt]]. */
  def isNullable: Boolean = underlying.isNullable

  override def toString: String = underlying.toString
