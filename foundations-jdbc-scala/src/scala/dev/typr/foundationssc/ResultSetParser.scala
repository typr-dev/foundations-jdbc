package dev.typr.foundationssc

import java.sql.ResultSet

/** Scala wrapper for dev.typr.foundations.ResultSetParser that provides Scala-native methods.
  *
  * Wraps the Java ResultSetParser to provide interop with Java APIs.
  */
class ResultSetParser[Out](val underlying: dev.typr.foundations.ResultSetParser[Out]) {
  def apply(rs: ResultSet): Out = underlying.apply(rs)

  def map[Out2](f: Out => Out2): ResultSetParser[Out2] =
    new ResultSetParser(underlying.map(f(_)))
}

object ResultSetParser {
  import scala.jdk.CollectionConverters.*

  /** Parse every row as a single column of the given type. */
  def all[T](dbType: DbType[T]): ResultSetParser[List[T]] =
    new ResultSetParser(dev.typr.foundations.RowCodec.of(dbType.underlying).all().map(_.asScala.toList))

  /** Parse exactly one row as a single column. Throws if zero or more than one row. */
  def exactlyOne[T](dbType: DbType[T]): ResultSetParser[T] =
    new ResultSetParser(dev.typr.foundations.RowCodec.of(dbType.underlying).exactlyOne())

  /** Parse at most one row as a single column, Some(v) if present, None if empty. */
  def maxOne[T](dbType: DbType[T]): ResultSetParser[Option[T]] =
    new ResultSetParser(dev.typr.foundations.RowCodec.of(dbType.underlying).maxOne().map(optToScala[T](_)))

  /** Parse the first row as a single column, Some(v) if present, None if empty. */
  def first[T](dbType: DbType[T]): ResultSetParser[Option[T]] =
    new ResultSetParser(dev.typr.foundations.RowCodec.of(dbType.underlying).first().map(optToScala[T](_)))

  private def optToScala[T](opt: java.util.Optional[T]): Option[T] =
    if opt.isPresent then Some(opt.get()) else None
}
