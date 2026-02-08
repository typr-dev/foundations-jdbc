package dev.typr.scalafoundations

import java.sql.ResultSet
import _root_.scala.jdk.CollectionConverters.*
import _root_.scala.jdk.OptionConverters.*

/** Scala wrapper for dev.typr.foundations.RowParser that provides Scala-native methods.
  *
  * This class has the same API surface as the Java RowParser but returns Scala types (Option[T]) instead of Java types (Optional[T]).
  */
class RowParser[Row](val underlying: dev.typr.foundations.RowParser[Row]) {

  /** Compose with another parser for INNER JOIN results.
    * Returns And[Row, Row2].
    */
  def joined[Row2](other: RowParser[Row2]): RowParser[dev.typr.foundations.And[Row, Row2]] =
    new RowParser(underlying.joined(other.underlying))

  /** Compose with another parser for LEFT JOIN results.
    * Returns And[Row, Option[Row2]] with Option right side (unlike Java which returns Optional).
    */
  def leftJoined[Row2](other: RowParser[Row2]): RowParser[dev.typr.foundations.And[Row, Option[Row2]]] = {
    val javaResult: dev.typr.foundations.RowParser[dev.typr.foundations.And[Row, java.util.Optional[Row2]]] =
      underlying.leftJoined(other.underlying)
    val converted = javaResult.to(Bijections.leftJoinToOption[Row, Row2])
    new RowParser(converted)
  }

  /** Compose with another parser for RIGHT JOIN results.
    * Returns And[Option[Row], Row2] with Option left side (unlike Java which returns Optional).
    */
  def rightJoined[Row2](other: RowParser[Row2]): RowParser[dev.typr.foundations.And[Option[Row], Row2]] = {
    val javaResult: dev.typr.foundations.RowParser[dev.typr.foundations.And[java.util.Optional[Row], Row2]] =
      underlying.rightJoined(other.underlying)
    val converted = javaResult.to(Bijections.rightJoinToOption[Row, Row2])
    new RowParser(converted)
  }

  /** Compose with another parser for FULL OUTER JOIN results.
    * Returns And[Option[Row], Option[Row2]] with both sides Option.
    */
  def fullJoined[Row2](other: RowParser[Row2]): RowParser[dev.typr.foundations.And[Option[Row], Option[Row2]]] = {
    val javaResult: dev.typr.foundations.RowParser[dev.typr.foundations.And[java.util.Optional[Row], java.util.Optional[Row2]]] =
      underlying.fullJoined(other.underlying)
    val converted = javaResult.to(Bijections.fullJoinToOption[Row, Row2])
    new RowParser(converted)
  }

  /** Parse all rows from a ResultSet. Returns Scala List instead of java.util.List.
    */
  def all(): ResultSetParser[List[Row]] = {
    val javaParser = underlying.all()
    new ResultSetParser(new dev.typr.foundations.ResultSetParser[List[Row]] {
      override def apply(rs: ResultSet): List[Row] = javaParser.apply(rs).asScala.toList
    })
  }

  /** Parse exactly one row from a ResultSet. Returns Row directly (throws if not exactly one row).
    */
  def exactlyOne(): ResultSetParser[Row] = {
    new ResultSetParser(underlying.exactlyOne())
  }

  /** Parse the first row from a ResultSet or None if empty. Returns Option[Row] instead of Optional[Row].
    */
  def first(): ResultSetParser[Option[Row]] = {
    val javaParser = new dev.typr.foundations.ResultSetParser.First(underlying)
    new ResultSetParser(new dev.typr.foundations.ResultSetParser[Option[Row]] {
      override def apply(rs: ResultSet): Option[Row] = javaParser.apply(rs).toScala
    })
  }

  /** Parse the first row from a ResultSet or None if empty. Alias for first() to match Java API.
    */
  def firstOrNone(): ResultSetParser[Option[Row]] = first()

  /** Parse at most one row from a ResultSet or None. Returns Option[Row] instead of Optional[Row].
    */
  def maxOne(): ResultSetParser[Option[Row]] = {
    val javaParser = new dev.typr.foundations.ResultSetParser.MaxOne(underlying)
    new ResultSetParser(new dev.typr.foundations.ResultSetParser[Option[Row]] {
      override def apply(rs: ResultSet): Option[Row] = javaParser.apply(rs).toScala
    })
  }

  /** Parse at most one row from a ResultSet or None. Alias for maxOne() to match Scala conventions.
    */
  def maxOneOrNone(): ResultSetParser[Option[Row]] = maxOne()

  /** Parse a single row from the current position in ResultSet.
    */
  def parse(rs: ResultSet): Row = underlying.parse(rs)

  /** Create a DbJson codec that encodes rows as JSON arrays.
    */
  def jsonArray(): dev.typr.foundations.DbJson[Row] =
    dev.typr.foundations.DbJsonRow.jsonArray(underlying)

  /** Create a DbJson codec that encodes rows as JSON objects with named fields.
    */
  def jsonObject(columnNames: List[String]): dev.typr.foundations.DbJson[Row] =
    import _root_.scala.jdk.CollectionConverters.*
    dev.typr.foundations.DbJsonRow.jsonObject(underlying, columnNames.asJava)
}

object RowParser {
  /** Create a type-safe builder for RowParser.
    */
  def builder[Row](): RowParserBuilders.Builder0[Row] = RowParserBuilders.builder[Row]()

  /** Create a single-column row parser.
    */
  def of[T](dbType: dev.typr.foundations.DbType[T]): RowParser[T] =
    new RowParser(dev.typr.foundations.RowParser.of(dbType))
}

/** Convert a Java RowParser to a Scala RowParser.
  */
extension [Row](javaParser: dev.typr.foundations.RowParser[Row]) {
  def asScalaRowParser: RowParser[Row] = new RowParser(javaParser)
}
