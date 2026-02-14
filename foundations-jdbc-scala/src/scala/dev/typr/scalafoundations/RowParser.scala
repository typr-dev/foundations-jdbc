package dev.typr.scalafoundations

import java.sql.ResultSet
import _root_.scala.jdk.CollectionConverters.*
import _root_.scala.jdk.OptionConverters.*

/** Scala wrapper for dev.typr.foundations.RowParser that provides Scala-native methods.
  *
  * This class has the same API surface as the Java RowParser but returns Scala types (Option[T]) instead of Java types (Optional[T]).
  */
class RowParser[Row](val underlying: dev.typr.foundations.RowParser[Row]) {

  def joined[Row2](other: RowParser[Row2]): RowParser[(Row, Row2)] =
    new RowParser(underlying.joined(other.underlying).to(Bijections.andToTuple[Row, Row2]))

  def leftJoined[Row2](other: RowParser[Row2]): RowParser[(Row, Option[Row2])] =
    new RowParser(underlying.leftJoined(other.underlying).to(Bijections.leftJoinToTuple[Row, Row2]))

  def rightJoined[Row2](other: RowParser[Row2]): RowParser[(Option[Row], Row2)] =
    new RowParser(underlying.rightJoined(other.underlying).to(Bijections.rightJoinToTuple[Row, Row2]))

  def fullJoined[Row2](other: RowParser[Row2]): RowParser[(Option[Row], Option[Row2])] =
    new RowParser(underlying.fullJoined(other.underlying).to(Bijections.fullJoinToTuple[Row, Row2]))

  /** Parse all rows from a ResultSet. Returns Scala List instead of java.util.List.
    */
  def all(): ResultSetParser[List[Row]] = {
    new ResultSetParser(underlying.all().map(jlist => jlist.asScala.toList))
  }

  /** Parse exactly one row from a ResultSet. Returns Row directly (throws if not exactly one row).
    */
  def exactlyOne(): ResultSetParser[Row] = {
    new ResultSetParser(underlying.exactlyOne())
  }

  /** Parse the first row from a ResultSet or None if empty. Returns Option[Row] instead of Optional[Row].
    */
  def first(): ResultSetParser[Option[Row]] = {
    new ResultSetParser(underlying.first().map(opt => opt.toScala))
  }

  /** Parse at most one row from a ResultSet or None. Returns Option[Row] instead of Optional[Row].
    */
  def maxOne(): ResultSetParser[Option[Row]] = {
    new ResultSetParser(underlying.maxOne().map(opt => opt.toScala))
  }

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

/** Scala wrapper for dev.typr.foundations.RowParserNamed.
  * Adds columnNames, columnList, and no-argument jsonObject().
  */
class RowParserNamed[Row](override val underlying: dev.typr.foundations.RowParserNamed[Row])
    extends RowParser[Row](underlying) {

  def columnNames: List[String] =
    import _root_.scala.jdk.CollectionConverters.*
    underlying.columnNames().asScala.toList

  def columnList: Fragment = new Fragment(underlying.columnList())

  def jsonObject(): dev.typr.foundations.DbJson[Row] =
    dev.typr.foundations.DbJsonRow.jsonObject(underlying)
}

object RowParser {
  /** Create a type-safe builder for RowParser.
    */
  def builder[Row](): RowParserBuilders.Builder0[Row] = RowParserBuilders.builder[Row]()

  /** Create a type-safe named builder for RowParser.
    */
  def namedBuilder[Row](): RowParserNamedBuilders.Builder0[Row] = RowParserNamedBuilders.builder[Row]()

  /** Create a single-column row parser.
    */
  def of[T](dbType: DbType[T]): RowParser[T] =
    new RowParser(dev.typr.foundations.RowParser.of(dbType.underlying))
}
