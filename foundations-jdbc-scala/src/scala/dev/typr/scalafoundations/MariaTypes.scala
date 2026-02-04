package dev.typr.scalafoundations

import dev.typr.foundations.{MariaType, MariaTypes => JavaMariaTypes}

/** Scala-friendly MariaType instances that use Scala types instead of Java boxed types.
  * All types from dev.typr.foundations.MariaTypes are available here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class MariaTypes {
  // Primitives - convert Java boxed types to Scala native types
  val tinyint: MariaType[Byte] = JavaMariaTypes.tinyint.bimap(b => b, b => b)
  val smallint: MariaType[Short] = JavaMariaTypes.smallint.bimap(s => s, s => s)
  val mediumint: MariaType[Int] = JavaMariaTypes.mediumint.bimap(i => i, i => i)
  val int_ : MariaType[Int] = JavaMariaTypes.int_.bimap(i => i, i => i)
  val bigint: MariaType[Long] = JavaMariaTypes.bigint.bimap(l => l, l => l)

  // Floating point
  val float_ : MariaType[Float] = JavaMariaTypes.float_.bimap(f => f, f => f)
  val double_ : MariaType[Double] = JavaMariaTypes.double_.bimap(d => d, d => d)

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: MariaType[BigDecimal] = JavaMariaTypes.decimal.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)
  val numeric: MariaType[BigDecimal] = JavaMariaTypes.numeric.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)

  // Boolean
  val bool: MariaType[Boolean] = JavaMariaTypes.bool.bimap(b => b, b => b)
  val bit1: MariaType[Boolean] = JavaMariaTypes.bit1.bimap(b => b, b => b)

  // Forward all other types directly from Java
  val tinyintUnsigned = JavaMariaTypes.tinyintUnsigned
  val smallintUnsigned = JavaMariaTypes.smallintUnsigned
  val mediumintUnsigned = JavaMariaTypes.mediumintUnsigned
  val intUnsigned = JavaMariaTypes.intUnsigned
  val bigintUnsigned = JavaMariaTypes.bigintUnsigned
  val bit = JavaMariaTypes.bit
  val char_ = JavaMariaTypes.char_
  val varchar = JavaMariaTypes.varchar
  val tinytext = JavaMariaTypes.tinytext
  val text = JavaMariaTypes.text
  val mediumtext = JavaMariaTypes.mediumtext
  val longtext = JavaMariaTypes.longtext
  val binary = JavaMariaTypes.binary
  val varbinary = JavaMariaTypes.varbinary
  val tinyblob = JavaMariaTypes.tinyblob
  val blob = JavaMariaTypes.blob
  val mediumblob = JavaMariaTypes.mediumblob
  val longblob = JavaMariaTypes.longblob
  val date = JavaMariaTypes.date
  val time = JavaMariaTypes.time
  val datetime = JavaMariaTypes.datetime
  val timestamp = JavaMariaTypes.timestamp
  val year = JavaMariaTypes.year
  val set = JavaMariaTypes.set
  val json = JavaMariaTypes.json
  val inet4 = JavaMariaTypes.inet4
  val inet6 = JavaMariaTypes.inet6
  val geometry = JavaMariaTypes.geometry
  val point = JavaMariaTypes.point
  val linestring = JavaMariaTypes.linestring
  val polygon = JavaMariaTypes.polygon
  val multipoint = JavaMariaTypes.multipoint
  val multilinestring = JavaMariaTypes.multilinestring
  val multipolygon = JavaMariaTypes.multipolygon
  val geometrycollection = JavaMariaTypes.geometrycollection
  val unknown = JavaMariaTypes.unknown

  // Forward static methods with Scala type conversion
  def decimal(precision: Int, scale: Int): MariaType[BigDecimal] =
    JavaMariaTypes.decimal(precision, scale).bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)

  def char_(length: Int): MariaType[String] =
    JavaMariaTypes.char_(length)

  def varchar(length: Int): MariaType[String] =
    JavaMariaTypes.varchar(length)

  def binary(length: Int): MariaType[Array[Byte]] =
    JavaMariaTypes.binary(length)

  def varbinary(length: Int): MariaType[Array[Byte]] =
    JavaMariaTypes.varbinary(length)

  def time(fsp: Int): MariaType[java.time.LocalTime] =
    JavaMariaTypes.time(fsp)

  def datetime(fsp: Int): MariaType[java.time.LocalDateTime] =
    JavaMariaTypes.datetime(fsp)

  def timestamp(fsp: Int): MariaType[java.time.LocalDateTime] =
    JavaMariaTypes.timestamp(fsp)

  def ofEnum[E <: Enum[E]](sqlType: String, fromString: java.util.function.Function[String, E]): MariaType[E] =
    JavaMariaTypes.ofEnum(sqlType, fromString)
}

object MariaTypes extends MariaTypes
