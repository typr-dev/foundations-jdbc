package dev.typr.foundations.scala

import dev.typr.foundations.{SqlServerType, SqlServerTypes => JavaSqlServerTypes}

/** Scala-friendly SqlServerType instances that use Scala types instead of Java boxed types.
  * All types from dev.typr.foundations.SqlServerTypes are available here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class SqlServerTypes {
  // Primitives - convert Java boxed types to Scala native types
  val smallint: SqlServerType[Short] = JavaSqlServerTypes.smallint.bimap(s => s, s => s)
  val int_ : SqlServerType[Int] = JavaSqlServerTypes.int_.bimap(i => i, i => i)
  val bigint: SqlServerType[Long] = JavaSqlServerTypes.bigint.bimap(l => l, l => l)
  val real: SqlServerType[Float] = JavaSqlServerTypes.real.bimap(f => f, f => f)
  val float_ : SqlServerType[Double] = JavaSqlServerTypes.float_.bimap(d => d, d => d)
  val bit: SqlServerType[Boolean] = JavaSqlServerTypes.bit.bimap(b => b, b => b)

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: SqlServerType[BigDecimal] = JavaSqlServerTypes.decimal.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)
  val numeric: SqlServerType[BigDecimal] = JavaSqlServerTypes.numeric.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)
  val money: SqlServerType[BigDecimal] = JavaSqlServerTypes.money.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)
  val smallmoney: SqlServerType[BigDecimal] = JavaSqlServerTypes.smallmoney.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)

  // Forward all other types directly from Java
  val tinyint = JavaSqlServerTypes.tinyint
  val char_ = JavaSqlServerTypes.char_
  val varchar = JavaSqlServerTypes.varchar
  val varcharMax = JavaSqlServerTypes.varcharMax
  val text = JavaSqlServerTypes.text
  val nchar = JavaSqlServerTypes.nchar
  val nvarchar = JavaSqlServerTypes.nvarchar
  val nvarcharMax = JavaSqlServerTypes.nvarcharMax
  val ntext = JavaSqlServerTypes.ntext
  val binary = JavaSqlServerTypes.binary
  val varbinary = JavaSqlServerTypes.varbinary
  val varbinaryMax = JavaSqlServerTypes.varbinaryMax
  val image = JavaSqlServerTypes.image
  val date = JavaSqlServerTypes.date
  val time = JavaSqlServerTypes.time
  val datetime = JavaSqlServerTypes.datetime
  val smalldatetime = JavaSqlServerTypes.smalldatetime
  val datetime2 = JavaSqlServerTypes.datetime2
  val datetimeoffset = JavaSqlServerTypes.datetimeoffset
  val uniqueidentifier = JavaSqlServerTypes.uniqueidentifier
  val xml = JavaSqlServerTypes.xml
  val json = JavaSqlServerTypes.json
  val vector = JavaSqlServerTypes.vector
  val rowversion = JavaSqlServerTypes.rowversion
  val timestamp = JavaSqlServerTypes.timestamp
  val hierarchyid = JavaSqlServerTypes.hierarchyid
  val sqlVariant = JavaSqlServerTypes.sqlVariant
  val geography = JavaSqlServerTypes.geography
  val geometry = JavaSqlServerTypes.geometry
  val unknown = JavaSqlServerTypes.unknown

  // Forward static methods
  def decimal(precision: Int, scale: Int): SqlServerType[java.math.BigDecimal] =
    JavaSqlServerTypes.decimal(precision, scale)

  def numeric(precision: Int, scale: Int): SqlServerType[java.math.BigDecimal] =
    JavaSqlServerTypes.numeric(precision, scale)

  def char_(length: Int): SqlServerType[String] =
    JavaSqlServerTypes.char_(length)

  def varchar(length: Int): SqlServerType[String] =
    JavaSqlServerTypes.varchar(length)

  def nchar(length: Int): SqlServerType[String] =
    JavaSqlServerTypes.nchar(length)

  def nvarchar(length: Int): SqlServerType[String] =
    JavaSqlServerTypes.nvarchar(length)

  def binary(length: Int): SqlServerType[Array[Byte]] =
    JavaSqlServerTypes.binary(length)

  def varbinary(length: Int): SqlServerType[Array[Byte]] =
    JavaSqlServerTypes.varbinary(length)

  def time(scale: Int): SqlServerType[java.time.LocalTime] =
    JavaSqlServerTypes.time(scale)

  def datetime2(scale: Int): SqlServerType[java.time.LocalDateTime] =
    JavaSqlServerTypes.datetime2(scale)

  def datetimeoffset(scale: Int): SqlServerType[java.time.OffsetDateTime] =
    JavaSqlServerTypes.datetimeoffset(scale)
}

object SqlServerTypes extends SqlServerTypes
