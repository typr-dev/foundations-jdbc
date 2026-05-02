package dev.typr.foundationssc

class SqliteType[T](override val underlying: dev.typr.foundations.SqliteType[T]) extends DbType[T](underlying):
  override def opt: SqliteType[Option[T]] =
    SqliteType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: Bijection[T, B]): SqliteType[B] =
    SqliteType(underlying.to(bijection))

  def transform[B](f: T => B, g: B => T): SqliteType[B] =
    SqliteType(underlying.transform(v => f(v), v => g(v)))

  def encode(value: T): Fragment = new Fragment(underlying.encode(value))

  def withTypename(typename: SqliteTypename[T]): SqliteType[T] = SqliteType(underlying.withTypename(typename))
  def withTypename(sqlType: String): SqliteType[T] = SqliteType(underlying.withTypename(sqlType))
  def renamed(value: String): SqliteType[T] = SqliteType(underlying.renamed(value))
  def renamedDropPrecision(value: String): SqliteType[T] = SqliteType(underlying.renamedDropPrecision(value))

  def withRead(read: SqliteRead[T]): SqliteType[T] = SqliteType(underlying.withRead(read))
  def withWrite(write: SqliteWrite[T]): SqliteType[T] = SqliteType(underlying.withWrite(write))
  def withJson(json: SqliteJson[T]): SqliteType[T] = SqliteType(underlying.withJson(json))
  def withAnalysis(opts: AnalysisOptions): SqliteType[T] = SqliteType(underlying.withAnalysis(opts))

  def unchecked(): SqliteType[T] = SqliteType(underlying.unchecked())
  def nullableOk(): SqliteType[T] = SqliteType(underlying.nullableOk())
