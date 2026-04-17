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
  val tinyintUnsigned = MariaType(JavaMariaTypes.tinyintUnsigned)
  val smallintUnsigned = MariaType(JavaMariaTypes.smallintUnsigned)
  val mediumintUnsigned = MariaType(JavaMariaTypes.mediumintUnsigned)
  val intUnsigned = MariaType(JavaMariaTypes.intUnsigned)
  val bigintUnsigned = MariaType(JavaMariaTypes.bigintUnsigned)
  val bit = MariaType(JavaMariaTypes.bit)
  val char_ = MariaType(JavaMariaTypes.char_)

  /** Alias for [[char_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val character = char_
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
  val uuid: MariaType[java.util.UUID] = MariaType(JavaMariaTypes.uuid)
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

  def vector(dimension: Int): MariaType[dev.typr.foundations.data.Vector] =
    MariaType(JavaMariaTypes.vector(dimension))

  /** Create a MariaType for an ENUM column, deriving the SQL literal from the enum class.
    *
    * Call-site: `MariaTypes.ofEnum[OrderState]` — no arguments, the enum class is resolved via
    * {{{ClassTag}}}. The column DDL must match the derived literal
    * (`ENUM('PENDING','SHIPPED',…)` using each enum constant's `name`). Use
    * [[ofEnum(sqlType, fromString)]] when the database labels differ from the Java enum's
    * `name()` values.
    *
    * See the Scala 3 enum caveat at the top of this file: simple Scala 3 enums need an explicit
    * `extends java.lang.Enum[E]` to satisfy the `<: Enum[E]` bound.
    */
  def ofEnum[E <: Enum[E]](using ct: scala.reflect.ClassTag[E]): MariaType[E] =
    MariaType(JavaMariaTypes.ofEnum(ct.runtimeClass.asInstanceOf[Class[E]]))

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
