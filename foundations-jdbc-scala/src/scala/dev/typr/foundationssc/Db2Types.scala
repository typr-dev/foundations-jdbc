package dev.typr.foundationssc

import dev.typr.foundations.{Db2Types => JavaDb2Types}

/** Scala-friendly Db2Type instances that use Scala types instead of Java boxed types. All types from dev.typr.foundations.Db2Types are available here, with
  * primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class Db2Types {
  // Primitives - convert Java boxed types to Scala native types
  val smallint: Db2Type[Short] = Db2Type(JavaDb2Types.smallint.transform(s => s, s => s))
  val integer: Db2Type[Int] = Db2Type(JavaDb2Types.integer.transform(i => i, i => i))
  val int_ : Db2Type[Int] = Db2Type(JavaDb2Types.int_.transform(i => i, i => i))
  val bigint: Db2Type[Long] = Db2Type(JavaDb2Types.bigint.transform(l => l, l => l))
  val real: Db2Type[Float] = Db2Type(JavaDb2Types.real.transform(f => f, f => f))

  /** Alias for [[real]] — aesthetic cross-palette naming. 4B IEEE 754. */
  val float4: Db2Type[Float] = real

  val double_ : Db2Type[Double] = Db2Type(JavaDb2Types.double_.transform(d => d, d => d))

  /** Alias for [[double_]] — aesthetic, avoids the Java-keyword `_` suffix. 8B IEEE 754. */
  val float8: Db2Type[Double] = double_

  val float_ : Db2Type[Double] = Db2Type(JavaDb2Types.float_.transform(d => d, d => d))
  val boolean_ : Db2Type[Boolean] = Db2Type(JavaDb2Types.boolean_.transform(b => b, b => b))

  /** Alias for [[boolean_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val bool: Db2Type[Boolean] = boolean_

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: Db2Type[BigDecimal] = Db2Type(JavaDb2Types.decimal.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val numeric: Db2Type[BigDecimal] = Db2Type(JavaDb2Types.numeric.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val dec: Db2Type[BigDecimal] = Db2Type(JavaDb2Types.dec.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val decfloat: Db2Type[BigDecimal] = Db2Type(JavaDb2Types.decfloat.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Forward all other types directly from Java
  val char_ = Db2Type(JavaDb2Types.char_)
  val character = Db2Type(JavaDb2Types.character)
  val varchar = Db2Type(JavaDb2Types.varchar)
  val clob = Db2Type(JavaDb2Types.clob)
  val graphic = Db2Type(JavaDb2Types.graphic)
  val vargraphic = Db2Type(JavaDb2Types.vargraphic)
  val dbclob = Db2Type(JavaDb2Types.dbclob)
  val binary = Db2Type(JavaDb2Types.binary)
  val varbinary = Db2Type(JavaDb2Types.varbinary)
  val blob = Db2Type(JavaDb2Types.blob)
  val date = Db2Type(JavaDb2Types.date)
  val time = Db2Type(JavaDb2Types.time)
  val timestamp = Db2Type(JavaDb2Types.timestamp)
  val xml = Db2Type(JavaDb2Types.xml)
  val rowid = Db2Type(JavaDb2Types.rowid)
  val `object` = Db2Type(JavaDb2Types.`object`)
  val unknown = Db2Type(JavaDb2Types.unknown)

  // Forward static methods with Scala type conversion
  def decimalOf(precision: Int, scale: Int): Db2Type[BigDecimal] =
    Db2Type(JavaDb2Types.decimalOf(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def numericOf(precision: Int, scale: Int): Db2Type[BigDecimal] =
    Db2Type(JavaDb2Types.numericOf(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def decfloatOf(precision: Int): Db2Type[BigDecimal] =
    Db2Type(JavaDb2Types.decfloatOf(precision).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def char_Of(length: Int): Db2Type[String] =
    Db2Type(JavaDb2Types.char_Of(length))

  def varcharOf(length: Int): Db2Type[String] =
    Db2Type(JavaDb2Types.varcharOf(length))

  def clobOf(length: Int): Db2Type[String] =
    Db2Type(JavaDb2Types.clobOf(length))

  def graphicOf(length: Int): Db2Type[String] =
    Db2Type(JavaDb2Types.graphicOf(length))

  def vargraphicOf(length: Int): Db2Type[String] =
    Db2Type(JavaDb2Types.vargraphicOf(length))

  def dbclobOf(length: Int): Db2Type[String] =
    Db2Type(JavaDb2Types.dbclobOf(length))

  def binaryOf(length: Int): Db2Type[Array[Byte]] =
    Db2Type(JavaDb2Types.binaryOf(length))

  def varbinaryOf(length: Int): Db2Type[Array[Byte]] =
    Db2Type(JavaDb2Types.varbinaryOf(length))

  def blobOf(length: Int): Db2Type[Array[Byte]] =
    Db2Type(JavaDb2Types.blobOf(length))

  def timestampOf(scale: Int): Db2Type[java.time.LocalDateTime] =
    Db2Type(JavaDb2Types.timestampOf(scale))

  val json = Db2Type(JavaDb2Types.json)

  // JSON-encoded row types

  def jsonArrayEncoded[Row](parser: RowCodec[Row]): Db2Type[Row] =
    Db2Type(JavaDb2Types.jsonArrayEncoded(parser.underlying))

  def jsonArrayEncodedList[Row](parser: RowCodec[Row]): Db2Type[List[Row]] =
    Db2Type(
      JavaDb2Types
        .jsonArrayEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )

  def jsonObjectEncoded[Row](parser: RowCodecNamed[Row]): Db2Type[Row] =
    Db2Type(JavaDb2Types.jsonObjectEncoded(parser.underlying))

  def jsonObjectEncodedList[Row](parser: RowCodecNamed[Row]): Db2Type[List[Row]] =
    Db2Type(
      JavaDb2Types
        .jsonObjectEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )
}

object Db2Types extends Db2Types
