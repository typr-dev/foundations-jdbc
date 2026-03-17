package dev.typr.foundationssc

import dev.typr.foundations.{MariaTypes => JavaMariaTypes}

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
  val bigint: MariaType[Long] = MariaType(JavaMariaTypes.bigint.transform(l => l, l => l))

  // Floating point
  val float_ : MariaType[Float] = MariaType(JavaMariaTypes.float_.transform(f => f, f => f))
  val double_ : MariaType[Double] = MariaType(JavaMariaTypes.double_.transform(d => d, d => d))

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: MariaType[BigDecimal] = MariaType(JavaMariaTypes.decimal.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val numeric: MariaType[BigDecimal] = MariaType(JavaMariaTypes.numeric.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Boolean
  val bool: MariaType[Boolean] = MariaType(JavaMariaTypes.bool.transform(b => b, b => b))
  val bit1: MariaType[Boolean] = MariaType(JavaMariaTypes.bit1.transform(b => b, b => b))

  // Forward all other types directly from Java
  val tinyintUnsigned = MariaType(JavaMariaTypes.tinyintUnsigned)
  val smallintUnsigned = MariaType(JavaMariaTypes.smallintUnsigned)
  val mediumintUnsigned = MariaType(JavaMariaTypes.mediumintUnsigned)
  val intUnsigned = MariaType(JavaMariaTypes.intUnsigned)
  val bigintUnsigned = MariaType(JavaMariaTypes.bigintUnsigned)
  val bit = MariaType(JavaMariaTypes.bit)
  val char_ = MariaType(JavaMariaTypes.char_)
  val varchar = MariaType(JavaMariaTypes.varchar)
  val tinytext = MariaType(JavaMariaTypes.tinytext)
  val text = MariaType(JavaMariaTypes.text)
  val mediumtext = MariaType(JavaMariaTypes.mediumtext)
  val longtext = MariaType(JavaMariaTypes.longtext)
  val binary = MariaType(JavaMariaTypes.binary)
  val varbinary = MariaType(JavaMariaTypes.varbinary)
  val tinyblob = MariaType(JavaMariaTypes.tinyblob)
  val blob = MariaType(JavaMariaTypes.blob)
  val mediumblob = MariaType(JavaMariaTypes.mediumblob)
  val longblob = MariaType(JavaMariaTypes.longblob)
  val date = MariaType(JavaMariaTypes.date)
  val time = MariaType(JavaMariaTypes.time)
  val datetime = MariaType(JavaMariaTypes.datetime)
  val timestamp = MariaType(JavaMariaTypes.timestamp)
  val year = MariaType(JavaMariaTypes.year)
  val set = MariaType(JavaMariaTypes.set)
  val json = MariaType(JavaMariaTypes.json)
  val inet4 = MariaType(JavaMariaTypes.inet4)
  val inet6 = MariaType(JavaMariaTypes.inet6)
  val geometry = MariaType(JavaMariaTypes.geometry)
  val point = MariaType(JavaMariaTypes.point)
  val linestring = MariaType(JavaMariaTypes.linestring)
  val polygon = MariaType(JavaMariaTypes.polygon)
  val multipoint = MariaType(JavaMariaTypes.multipoint)
  val multilinestring = MariaType(JavaMariaTypes.multilinestring)
  val multipolygon = MariaType(JavaMariaTypes.multipolygon)
  val geometrycollection = MariaType(JavaMariaTypes.geometrycollection)
  val unknown = MariaType(JavaMariaTypes.unknown)

  // Forward static methods with Scala type conversion
  def decimal(precision: Int, scale: Int): MariaType[BigDecimal] =
    MariaType(JavaMariaTypes.decimal(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def char_(length: Int): MariaType[String] =
    MariaType(JavaMariaTypes.char_(length))

  def varchar(length: Int): MariaType[String] =
    MariaType(JavaMariaTypes.varchar(length))

  def binary(length: Int): MariaType[Array[Byte]] =
    MariaType(JavaMariaTypes.binary(length))

  def varbinary(length: Int): MariaType[Array[Byte]] =
    MariaType(JavaMariaTypes.varbinary(length))

  def time(fsp: Int): MariaType[java.time.LocalTime] =
    MariaType(JavaMariaTypes.time(fsp))

  def datetime(fsp: Int): MariaType[java.time.LocalDateTime] =
    MariaType(JavaMariaTypes.datetime(fsp))

  def timestamp(fsp: Int): MariaType[java.time.LocalDateTime] =
    MariaType(JavaMariaTypes.timestamp(fsp))

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
