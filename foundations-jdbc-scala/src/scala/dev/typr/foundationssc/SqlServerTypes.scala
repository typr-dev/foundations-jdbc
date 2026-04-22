package dev.typr.foundationssc

import dev.typr.foundations.{SqlServerTypes => JavaSqlServerTypes}
import dev.typr.foundations.data.{Json, Uint1, Unknown, Xml}
import dev.typr.foundationssc.data.*

/** Scala-friendly SqlServerType instances that use Scala types instead of Java boxed types. All types from dev.typr.foundations.SqlServerTypes are available
  * here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class SqlServerTypes {
  // Primitives - convert Java boxed types to Scala native types
  val smallint: SqlServerType[Short] = SqlServerType(JavaSqlServerTypes.smallint.transform(s => s, s => s))
  val int_ : SqlServerType[Int] = SqlServerType(JavaSqlServerTypes.int_.transform(i => i, i => i))

  /** Alias for [[int_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val integer: SqlServerType[Int] = int_

  val bigint: SqlServerType[Long] = SqlServerType(JavaSqlServerTypes.bigint.transform(l => l, l => l))
  val real: SqlServerType[Float] = SqlServerType(JavaSqlServerTypes.real.transform(f => f, f => f))

  /** Alias for [[real]] — aesthetic cross-palette naming. 4B IEEE 754. */
  val float4: SqlServerType[Float] = real

  val float_ : SqlServerType[Double] = SqlServerType(JavaSqlServerTypes.float_.transform(d => d, d => d))

  /** Alias for [[float_]] — aesthetic, avoids the Java-keyword `_` suffix. 8B IEEE 754 (FLOAT ≡ FLOAT(53)). */
  val float8: SqlServerType[Double] = float_
  val bit: SqlServerType[Boolean] = SqlServerType(JavaSqlServerTypes.bit.transform(b => b, b => b))

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: SqlServerType[BigDecimal] = SqlServerType(JavaSqlServerTypes.decimal.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val numeric: SqlServerType[BigDecimal] = SqlServerType(JavaSqlServerTypes.numeric.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val money: SqlServerType[BigDecimal] = SqlServerType(JavaSqlServerTypes.money.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val smallmoney: SqlServerType[BigDecimal] = SqlServerType(JavaSqlServerTypes.smallmoney.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Forward all other types directly from Java
  val tinyint: SqlServerType[Uint1] = SqlServerType(JavaSqlServerTypes.tinyint)
  val char_ : SqlServerType[String] = SqlServerType(JavaSqlServerTypes.char_)

  /** Alias for [[char_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val character: SqlServerType[String] = char_
  val varchar: SqlServerType[String] = SqlServerType(JavaSqlServerTypes.varchar)
  val varcharMax: SqlServerType[String] = SqlServerType(JavaSqlServerTypes.varcharMax)
  val text: SqlServerType[String] = SqlServerType(JavaSqlServerTypes.text)
  val nchar: SqlServerType[String] = SqlServerType(JavaSqlServerTypes.nchar)
  val nvarchar: SqlServerType[String] = SqlServerType(JavaSqlServerTypes.nvarchar)
  val nvarcharMax: SqlServerType[String] = SqlServerType(JavaSqlServerTypes.nvarcharMax)
  val ntext: SqlServerType[String] = SqlServerType(JavaSqlServerTypes.ntext)
  val binary: SqlServerType[Array[Byte]] = SqlServerType(JavaSqlServerTypes.binary)
  val varbinary: SqlServerType[Array[Byte]] = SqlServerType(JavaSqlServerTypes.varbinary)
  val varbinaryMax: SqlServerType[Array[Byte]] = SqlServerType(JavaSqlServerTypes.varbinaryMax)
  val image: SqlServerType[Array[Byte]] = SqlServerType(JavaSqlServerTypes.image)
  val date: SqlServerType[java.time.LocalDate] = SqlServerType(JavaSqlServerTypes.date)
  val time: SqlServerType[java.time.LocalTime] = SqlServerType(JavaSqlServerTypes.time)
  val datetime: SqlServerType[java.time.LocalDateTime] = SqlServerType(JavaSqlServerTypes.datetime)
  val smalldatetime: SqlServerType[java.time.LocalDateTime] = SqlServerType(JavaSqlServerTypes.smalldatetime)
  val datetime2: SqlServerType[java.time.LocalDateTime] = SqlServerType(JavaSqlServerTypes.datetime2)
  val datetimeoffset: SqlServerType[java.time.OffsetDateTime] = SqlServerType(JavaSqlServerTypes.datetimeoffset)
  val uniqueidentifier: SqlServerType[java.util.UUID] = SqlServerType(JavaSqlServerTypes.uniqueidentifier)
  val xml: SqlServerType[Xml] = SqlServerType(JavaSqlServerTypes.xml)
  val json: SqlServerType[Json] = SqlServerType(JavaSqlServerTypes.json)
  val vector: SqlServerType[Array[Byte]] = SqlServerType(JavaSqlServerTypes.vector)
  val rowversion: SqlServerType[Array[Byte]] = SqlServerType(JavaSqlServerTypes.rowversion)
  val timestamp: SqlServerType[Array[Byte]] = SqlServerType(JavaSqlServerTypes.timestamp)
  val hierarchyid: SqlServerType[HierarchyId] = SqlServerType(JavaSqlServerTypes.hierarchyid)
  val sqlVariant: SqlServerType[AnyRef] = SqlServerType(JavaSqlServerTypes.sqlVariant)
  val geography: SqlServerType[com.microsoft.sqlserver.jdbc.Geography] = SqlServerType(JavaSqlServerTypes.geography)
  val geometry: SqlServerType[com.microsoft.sqlserver.jdbc.Geometry] = SqlServerType(JavaSqlServerTypes.geometry)
  val unknown: SqlServerType[Unknown] = SqlServerType(JavaSqlServerTypes.unknown)

  // Forward static methods with Scala type conversion
  def decimalOf(precision: Int, scale: Int): SqlServerType[BigDecimal] =
    SqlServerType(JavaSqlServerTypes.decimalOf(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def numericOf(precision: Int, scale: Int): SqlServerType[BigDecimal] =
    SqlServerType(JavaSqlServerTypes.numericOf(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def char_Of(length: Int): SqlServerType[String] =
    SqlServerType(JavaSqlServerTypes.char_Of(length))

  def varcharOf(length: Int): SqlServerType[String] =
    SqlServerType(JavaSqlServerTypes.varcharOf(length))

  def ncharOf(length: Int): SqlServerType[String] =
    SqlServerType(JavaSqlServerTypes.ncharOf(length))

  def nvarcharOf(length: Int): SqlServerType[String] =
    SqlServerType(JavaSqlServerTypes.nvarcharOf(length))

  def binaryOf(length: Int): SqlServerType[Array[Byte]] =
    SqlServerType(JavaSqlServerTypes.binaryOf(length))

  def varbinaryOf(length: Int): SqlServerType[Array[Byte]] =
    SqlServerType(JavaSqlServerTypes.varbinaryOf(length))

  def timeOf(scale: Int): SqlServerType[java.time.LocalTime] =
    SqlServerType(JavaSqlServerTypes.timeOf(scale))

  def datetime2Of(scale: Int): SqlServerType[java.time.LocalDateTime] =
    SqlServerType(JavaSqlServerTypes.datetime2Of(scale))

  def datetimeoffsetOf(scale: Int): SqlServerType[java.time.OffsetDateTime] =
    SqlServerType(JavaSqlServerTypes.datetimeoffsetOf(scale))

  // JSON-encoded row types

  def jsonArrayEncoded[Row](parser: RowCodec[Row]): SqlServerType[Row] =
    SqlServerType(JavaSqlServerTypes.jsonArrayEncoded(parser.underlying))

  def jsonArrayEncodedList[Row](parser: RowCodec[Row]): SqlServerType[List[Row]] =
    SqlServerType(
      JavaSqlServerTypes
        .jsonArrayEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )

  def jsonObjectEncoded[Row](parser: RowCodecNamed[Row]): SqlServerType[Row] =
    SqlServerType(JavaSqlServerTypes.jsonObjectEncoded(parser.underlying))

  def jsonObjectEncodedList[Row](parser: RowCodecNamed[Row]): SqlServerType[List[Row]] =
    SqlServerType(
      JavaSqlServerTypes
        .jsonObjectEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )
  def ofEnum[E <: AnyRef, U](underlying: SqlServerType[U], values: Array[E], toUnderlying: java.util.function.Function[E, U]): SqlServerType[E] =
    SqlServerType(JavaSqlServerTypes.ofEnum(underlying.underlying, values.asInstanceOf[Array[Object & E]], toUnderlying))

  def ofEnum[E <: Enum[E]](values: Array[E]): SqlServerType[E] =
    SqlServerType(JavaSqlServerTypes.ofEnum(values))

  def ofEnum[E <: AnyRef](values: Array[E]): SqlServerType[E] =
    ofEnum(SqlServerTypes.nvarchar, values, ((e: E) => e.toString): java.util.function.Function[E, String])

}

object SqlServerTypes extends SqlServerTypes
