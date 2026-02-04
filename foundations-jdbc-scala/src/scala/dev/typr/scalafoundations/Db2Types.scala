package dev.typr.scalafoundations

import dev.typr.foundations.{Db2Type, Db2Types => JavaDb2Types}

/** Scala-friendly Db2Type instances that use Scala types instead of Java boxed types.
  * All types from dev.typr.foundations.Db2Types are available here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class Db2Types {
  // Primitives - convert Java boxed types to Scala native types
  val smallint: Db2Type[Short] = JavaDb2Types.smallint.bimap(s => s, s => s)
  val integer: Db2Type[Int] = JavaDb2Types.integer.bimap(i => i, i => i)
  val int_ : Db2Type[Int] = JavaDb2Types.int_.bimap(i => i, i => i)
  val bigint: Db2Type[Long] = JavaDb2Types.bigint.bimap(l => l, l => l)
  val real: Db2Type[Float] = JavaDb2Types.real.bimap(f => f, f => f)
  val double_ : Db2Type[Double] = JavaDb2Types.double_.bimap(d => d, d => d)
  val float_ : Db2Type[Double] = JavaDb2Types.float_.bimap(d => d, d => d)
  val boolean_ : Db2Type[Boolean] = JavaDb2Types.boolean_.bimap(b => b, b => b)

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: Db2Type[BigDecimal] = JavaDb2Types.decimal.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)
  val numeric: Db2Type[BigDecimal] = JavaDb2Types.numeric.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)
  val dec: Db2Type[BigDecimal] = JavaDb2Types.dec.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)
  val decfloat: Db2Type[BigDecimal] = JavaDb2Types.decfloat.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)

  // Forward all other types directly from Java
  val char_ = JavaDb2Types.char_
  val character = JavaDb2Types.character
  val varchar = JavaDb2Types.varchar
  val clob = JavaDb2Types.clob
  val graphic = JavaDb2Types.graphic
  val vargraphic = JavaDb2Types.vargraphic
  val dbclob = JavaDb2Types.dbclob
  val binary = JavaDb2Types.binary
  val varbinary = JavaDb2Types.varbinary
  val blob = JavaDb2Types.blob
  val date = JavaDb2Types.date
  val time = JavaDb2Types.time
  val timestamp = JavaDb2Types.timestamp
  val xml = JavaDb2Types.xml
  val rowid = JavaDb2Types.rowid
  val `object` = JavaDb2Types.`object`
  val unknown = JavaDb2Types.unknown

  // Forward static methods with Scala type conversion
  def decimal(precision: Int, scale: Int): Db2Type[BigDecimal] =
    JavaDb2Types.decimal(precision, scale).bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)

  def numeric(precision: Int, scale: Int): Db2Type[BigDecimal] =
    JavaDb2Types.numeric(precision, scale).bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)

  def decfloat(precision: Int): Db2Type[BigDecimal] =
    JavaDb2Types.decfloat(precision).bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)

  def char_(length: Int): Db2Type[String] =
    JavaDb2Types.char_(length)

  def varchar(length: Int): Db2Type[String] =
    JavaDb2Types.varchar(length)

  def clob(length: Int): Db2Type[String] =
    JavaDb2Types.clob(length)

  def graphic(length: Int): Db2Type[String] =
    JavaDb2Types.graphic(length)

  def vargraphic(length: Int): Db2Type[String] =
    JavaDb2Types.vargraphic(length)

  def dbclob(length: Int): Db2Type[String] =
    JavaDb2Types.dbclob(length)

  def binary(length: Int): Db2Type[Array[Byte]] =
    JavaDb2Types.binary(length)

  def varbinary(length: Int): Db2Type[Array[Byte]] =
    JavaDb2Types.varbinary(length)

  def blob(length: Int): Db2Type[Array[Byte]] =
    JavaDb2Types.blob(length)

  def timestamp(scale: Int): Db2Type[java.time.LocalDateTime] =
    JavaDb2Types.timestamp(scale)
}

object Db2Types extends Db2Types
