package dev.typr.foundationssc

import java.sql.ResultSet
import _root_.scala.jdk.CollectionConverters.*
import _root_.scala.jdk.OptionConverters.*

/** Scala wrapper for dev.typr.foundations.RowCodec that provides Scala-native methods.
  *
  * This class has the same API surface as the Java RowCodec but returns Scala types (Option[T]) instead of Java types (Optional[T]).
  */
class RowCodec[Row](val underlying: dev.typr.foundations.RowCodec[Row]) {

  def joined[Row2](other: RowCodec[Row2]): RowCodec[(Row, Row2)] =
    new RowCodec(underlying.joined(other.underlying).to(Bijections.andToTuple[Row, Row2]))

  def leftJoined[Row2](other: RowCodec[Row2]): RowCodec[(Row, Option[Row2])] =
    new RowCodec(underlying.leftJoined(other.underlying).to(Bijections.leftJoinToTuple[Row, Row2]))

  def rightJoined[Row2](other: RowCodec[Row2]): RowCodec[(Option[Row], Row2)] =
    new RowCodec(underlying.rightJoined(other.underlying).to(Bijections.rightJoinToTuple[Row, Row2]))

  def fullJoined[Row2](other: RowCodec[Row2]): RowCodec[(Option[Row], Option[Row2])] =
    new RowCodec(underlying.fullJoined(other.underlying).to(Bijections.fullJoinToTuple[Row, Row2]))

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

/** Scala wrapper for dev.typr.foundations.RowCodecNamed.
  * Adds columnNames, columnList, and no-argument jsonObject().
  */
class RowCodecNamed[Row](override val underlying: dev.typr.foundations.RowCodecNamed[Row])
    extends RowCodec[Row](underlying) {

  def columnNames: List[String] =
    import _root_.scala.jdk.CollectionConverters.*
    underlying.columnNames().asScala.toList

  def columnList: Fragment = new Fragment(underlying.columnList())

  def join[Row2](other: RowCodecNamed[Row2]): RowCodecNamed[(Row, Row2)] =
    new RowCodecNamed(underlying.join(other.underlying).to(Bijections.andToTuple[Row, Row2]))

  def to[Row2](forward: Row => Row2, backward: Row2 => Row): RowCodecNamed[Row2] =
    new RowCodecNamed(underlying.to(dev.typr.foundations.Bijection.of[Row, Row2](r => forward(r), r2 => backward(r2))))

  def jsonObject(): dev.typr.foundations.DbJson[Row] =
    dev.typr.foundations.DbJsonRow.jsonObject(underlying)
}

object RowCodec {
  /** Create a type-safe builder for RowCodec.
    */
  def builder[Row](): RowCodecBuilders.Builder0[Row] = RowCodecBuilders.builder[Row]()

  /** Create a type-safe named builder for RowCodec.
    */
  def namedBuilder[Row](): RowCodecNamedBuilders.Builder0[Row] = RowCodecNamedBuilders.builder[Row]()

  /** Create a single-column row parser.
    */
  def of[T](dbType: DbType[T]): RowCodec[T] =
    new RowCodec(dev.typr.foundations.RowCodec.of(dbType.underlying))

  /** Create a single-column named row codec.
    */
  def ofNamed[T](name: String, dbType: DbType[T]): RowCodecNamed[T] =
    new RowCodecNamed(dev.typr.foundations.RowCodec.ofNamed(name, dbType.underlying))
}
