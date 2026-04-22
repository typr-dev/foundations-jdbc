package dev.typr.foundationssc

import dev.typr.foundations.{MariaTypes => JavaMariaTypes}
import dev.typr.foundations.data.{Json, Uint1, Uint2, Uint4, Uint8, Unknown}
import dev.typr.foundationssc.data.*

/** Scala-friendly MariaType instances that use Scala types instead of Java boxed types. All types from dev.typr.foundations.MariaTypes are available here, with
  * primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class MariaTypes {
  // Primitives - convert Java boxed types to Scala native types
  val tinyint: MariaType[Byte] = MariaType(JavaMariaTypes.tinyint.transform(b => b, b => b))
  val smallint: MariaType[Short] = MariaType(JavaMariaTypes.smallint.transform(s => s, s => s))
  val mediumint: MariaType[Int] = MariaType(JavaMariaTypes.mediumint.transform(i => i, i => i))
  val int_ : MariaType[Int] = MariaType(JavaMariaTypes.int_.transform(i => i, i => i))

  /** Alias for [[int_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val integer: MariaType[Int] = int_

  val bigint: MariaType[Long] = MariaType(JavaMariaTypes.bigint.transform(l => l, l => l))

  // Floating point
  val float_ : MariaType[Float] = MariaType(JavaMariaTypes.float_.transform(f => f, f => f))

  /** Alias for [[float_]] — aesthetic cross-palette naming. 4B IEEE 754. */
  val float4: MariaType[Float] = float_

  val double_ : MariaType[Double] = MariaType(JavaMariaTypes.double_.transform(d => d, d => d))

  /** Alias for [[double_]] — aesthetic, avoids the Java-keyword `_` suffix. 8B IEEE 754. */
  val float8: MariaType[Double] = double_

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: MariaType[BigDecimal] = MariaType(JavaMariaTypes.decimal.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val numeric: MariaType[BigDecimal] = MariaType(JavaMariaTypes.numeric.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Boolean
  val bool: MariaType[Boolean] = MariaType(JavaMariaTypes.bool.transform(b => b, b => b))
  val bit1: MariaType[Boolean] = MariaType(JavaMariaTypes.bit1.transform(b => b, b => b))

  // Forward all other types directly from Java
  val tinyintUnsigned: MariaType[Uint1] = MariaType(JavaMariaTypes.tinyintUnsigned)
  val smallintUnsigned: MariaType[Uint2] = MariaType(JavaMariaTypes.smallintUnsigned)
  val mediumintUnsigned: MariaType[Uint4] = MariaType(JavaMariaTypes.mediumintUnsigned)
  val intUnsigned: MariaType[Uint4] = MariaType(JavaMariaTypes.intUnsigned)
  val bigintUnsigned: MariaType[Uint8] = MariaType(JavaMariaTypes.bigintUnsigned)
  val bit: MariaType[Array[Byte]] = MariaType(JavaMariaTypes.bit)
  val char_ : MariaType[String] = MariaType(JavaMariaTypes.char_)

  /** Alias for [[char_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val character: MariaType[String] = char_
  val varchar: MariaType[String] = MariaType(JavaMariaTypes.varchar)
  val tinytext: MariaType[String] = MariaType(JavaMariaTypes.tinytext)
  val text: MariaType[String] = MariaType(JavaMariaTypes.text)
  val mediumtext: MariaType[String] = MariaType(JavaMariaTypes.mediumtext)
  val longtext: MariaType[String] = MariaType(JavaMariaTypes.longtext)
  val binary: MariaType[Array[Byte]] = MariaType(JavaMariaTypes.binary)
  val varbinary: MariaType[Array[Byte]] = MariaType(JavaMariaTypes.varbinary)
  val tinyblob: MariaType[Array[Byte]] = MariaType(JavaMariaTypes.tinyblob)
  val blob: MariaType[Array[Byte]] = MariaType(JavaMariaTypes.blob)
  val mediumblob: MariaType[Array[Byte]] = MariaType(JavaMariaTypes.mediumblob)
  val longblob: MariaType[Array[Byte]] = MariaType(JavaMariaTypes.longblob)
  val date: MariaType[java.time.LocalDate] = MariaType(JavaMariaTypes.date)
  val time: MariaType[java.time.LocalTime] = MariaType(JavaMariaTypes.time)
  val datetime: MariaType[java.time.LocalDateTime] = MariaType(JavaMariaTypes.datetime)
  val timestamp: MariaType[java.time.LocalDateTime] = MariaType(JavaMariaTypes.timestamp)
  val year: MariaType[java.time.Year] = MariaType(JavaMariaTypes.year)
  val set: MariaType[MariaSet] = MariaType(JavaMariaTypes.set)
  val json: MariaType[Json] = MariaType(JavaMariaTypes.json)
  val inet4: MariaType[Inet4] = MariaType(JavaMariaTypes.inet4)
  val inet6: MariaType[Inet6] = MariaType(JavaMariaTypes.inet6)
  val uuid: MariaType[java.util.UUID] = MariaType(JavaMariaTypes.uuid)
  val geometry: MariaType[org.mariadb.jdbc.`type`.Geometry] = MariaType(JavaMariaTypes.geometry)
  val point: MariaType[org.mariadb.jdbc.`type`.Point] = MariaType(JavaMariaTypes.point)
  val linestring: MariaType[org.mariadb.jdbc.`type`.LineString] = MariaType(JavaMariaTypes.linestring)
  val polygon: MariaType[org.mariadb.jdbc.`type`.Polygon] = MariaType(JavaMariaTypes.polygon)
  val multipoint: MariaType[org.mariadb.jdbc.`type`.MultiPoint] = MariaType(JavaMariaTypes.multipoint)
  val multilinestring: MariaType[org.mariadb.jdbc.`type`.MultiLineString] = MariaType(JavaMariaTypes.multilinestring)
  val multipolygon: MariaType[org.mariadb.jdbc.`type`.MultiPolygon] = MariaType(JavaMariaTypes.multipolygon)
  val geometrycollection: MariaType[org.mariadb.jdbc.`type`.GeometryCollection] = MariaType(JavaMariaTypes.geometrycollection)
  val unknown: MariaType[Unknown] = MariaType(JavaMariaTypes.unknown)

  // Forward static methods with Scala type conversion
  def decimalOf(precision: Int, scale: Int): MariaType[BigDecimal] =
    MariaType(JavaMariaTypes.decimalOf(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def char_Of(length: Int): MariaType[String] =
    MariaType(JavaMariaTypes.char_Of(length))

  def varcharOf(length: Int): MariaType[String] =
    MariaType(JavaMariaTypes.varcharOf(length))

  def binaryOf(length: Int): MariaType[Array[Byte]] =
    MariaType(JavaMariaTypes.binaryOf(length))

  def varbinaryOf(length: Int): MariaType[Array[Byte]] =
    MariaType(JavaMariaTypes.varbinaryOf(length))

  def timeOf(fsp: Int): MariaType[java.time.LocalTime] =
    MariaType(JavaMariaTypes.timeOf(fsp))

  def datetimeOf(fsp: Int): MariaType[java.time.LocalDateTime] =
    MariaType(JavaMariaTypes.datetimeOf(fsp))

  def timestampOf(fsp: Int): MariaType[java.time.LocalDateTime] =
    MariaType(JavaMariaTypes.timestampOf(fsp))

  def vector(dimension: Int): MariaType[dev.typr.foundationssc.data.Vector] =
    MariaType(JavaMariaTypes.vector(dimension))

  def ofEnum[E <: AnyRef](values: Array[E]): MariaType[E] =
    ofEnum(values, ((e: E) => e.toString): java.util.function.Function[E, String])

  def ofEnum[E <: AnyRef](values: Array[E], name: java.util.function.Function[E, String]): MariaType[E] =
    MariaType(JavaMariaTypes.ofEnum(values.asInstanceOf[Array[Object & E]], name))

  def ofEnum[E <: Enum[E]](sqlType: String, fromString: java.util.function.Function[String, E]): MariaType[E] =
    MariaType(JavaMariaTypes.ofEnum(sqlType, fromString))

  // JSON-encoded row types

  def jsonArrayEncoded[Row](parser: RowCodec[Row]): MariaType[Row] =
    MariaType(JavaMariaTypes.jsonArrayEncoded(parser.underlying))

  def jsonArrayEncodedList[Row](parser: RowCodec[Row]): MariaType[List[Row]] =
    MariaType(
      JavaMariaTypes
        .jsonArrayEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )

  def jsonObjectEncoded[Row](parser: RowCodecNamed[Row]): MariaType[Row] =
    MariaType(JavaMariaTypes.jsonObjectEncoded(parser.underlying))

  def jsonObjectEncodedList[Row](parser: RowCodecNamed[Row]): MariaType[List[Row]] =
    MariaType(
      JavaMariaTypes
        .jsonObjectEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )
}

object MariaTypes extends MariaTypes
