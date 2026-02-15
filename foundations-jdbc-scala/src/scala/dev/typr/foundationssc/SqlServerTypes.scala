package dev.typr.foundationssc

import dev.typr.foundations.{SqlServerTypes => JavaSqlServerTypes}

/** Scala-friendly SqlServerType instances that use Scala types instead of Java boxed types.
  * All types from dev.typr.foundations.SqlServerTypes are available here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class SqlServerTypes {
  // Primitives - convert Java boxed types to Scala native types
  val smallint: SqlServerType[Short] = SqlServerType(JavaSqlServerTypes.smallint.transform(s => s, s => s))
  val int_ : SqlServerType[Int] = SqlServerType(JavaSqlServerTypes.int_.transform(i => i, i => i))
  val bigint: SqlServerType[Long] = SqlServerType(JavaSqlServerTypes.bigint.transform(l => l, l => l))
  val real: SqlServerType[Float] = SqlServerType(JavaSqlServerTypes.real.transform(f => f, f => f))
  val float_ : SqlServerType[Double] = SqlServerType(JavaSqlServerTypes.float_.transform(d => d, d => d))
  val bit: SqlServerType[Boolean] = SqlServerType(JavaSqlServerTypes.bit.transform(b => b, b => b))

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: SqlServerType[BigDecimal] = SqlServerType(JavaSqlServerTypes.decimal.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val numeric: SqlServerType[BigDecimal] = SqlServerType(JavaSqlServerTypes.numeric.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val money: SqlServerType[BigDecimal] = SqlServerType(JavaSqlServerTypes.money.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val smallmoney: SqlServerType[BigDecimal] = SqlServerType(JavaSqlServerTypes.smallmoney.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Forward all other types directly from Java
  val tinyint = SqlServerType(JavaSqlServerTypes.tinyint)
  val char_ = SqlServerType(JavaSqlServerTypes.char_)
  val varchar = SqlServerType(JavaSqlServerTypes.varchar)
  val varcharMax = SqlServerType(JavaSqlServerTypes.varcharMax)
  val text = SqlServerType(JavaSqlServerTypes.text)
  val nchar = SqlServerType(JavaSqlServerTypes.nchar)
  val nvarchar = SqlServerType(JavaSqlServerTypes.nvarchar)
  val nvarcharMax = SqlServerType(JavaSqlServerTypes.nvarcharMax)
  val ntext = SqlServerType(JavaSqlServerTypes.ntext)
  val binary = SqlServerType(JavaSqlServerTypes.binary)
  val varbinary = SqlServerType(JavaSqlServerTypes.varbinary)
  val varbinaryMax = SqlServerType(JavaSqlServerTypes.varbinaryMax)
  val image = SqlServerType(JavaSqlServerTypes.image)
  val date = SqlServerType(JavaSqlServerTypes.date)
  val time = SqlServerType(JavaSqlServerTypes.time)
  val datetime = SqlServerType(JavaSqlServerTypes.datetime)
  val smalldatetime = SqlServerType(JavaSqlServerTypes.smalldatetime)
  val datetime2 = SqlServerType(JavaSqlServerTypes.datetime2)
  val datetimeoffset = SqlServerType(JavaSqlServerTypes.datetimeoffset)
  val uniqueidentifier = SqlServerType(JavaSqlServerTypes.uniqueidentifier)
  val xml = SqlServerType(JavaSqlServerTypes.xml)
  val json = SqlServerType(JavaSqlServerTypes.json)
  val vector = SqlServerType(JavaSqlServerTypes.vector)
  val rowversion = SqlServerType(JavaSqlServerTypes.rowversion)
  val timestamp = SqlServerType(JavaSqlServerTypes.timestamp)
  val hierarchyid = SqlServerType(JavaSqlServerTypes.hierarchyid)
  val sqlVariant = SqlServerType(JavaSqlServerTypes.sqlVariant)
  val geography = SqlServerType(JavaSqlServerTypes.geography)
  val geometry = SqlServerType(JavaSqlServerTypes.geometry)
  val unknown = SqlServerType(JavaSqlServerTypes.unknown)

  // Forward static methods with Scala type conversion
  def decimal(precision: Int, scale: Int): SqlServerType[BigDecimal] =
    SqlServerType(JavaSqlServerTypes.decimal(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def numeric(precision: Int, scale: Int): SqlServerType[BigDecimal] =
    SqlServerType(JavaSqlServerTypes.numeric(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def char_(length: Int): SqlServerType[String] =
    SqlServerType(JavaSqlServerTypes.char_(length))

  def varchar(length: Int): SqlServerType[String] =
    SqlServerType(JavaSqlServerTypes.varchar(length))

  def nchar(length: Int): SqlServerType[String] =
    SqlServerType(JavaSqlServerTypes.nchar(length))

  def nvarchar(length: Int): SqlServerType[String] =
    SqlServerType(JavaSqlServerTypes.nvarchar(length))

  def binary(length: Int): SqlServerType[Array[Byte]] =
    SqlServerType(JavaSqlServerTypes.binary(length))

  def varbinary(length: Int): SqlServerType[Array[Byte]] =
    SqlServerType(JavaSqlServerTypes.varbinary(length))

  def time(scale: Int): SqlServerType[java.time.LocalTime] =
    SqlServerType(JavaSqlServerTypes.time(scale))

  def datetime2(scale: Int): SqlServerType[java.time.LocalDateTime] =
    SqlServerType(JavaSqlServerTypes.datetime2(scale))

  def datetimeoffset(scale: Int): SqlServerType[java.time.OffsetDateTime] =
    SqlServerType(JavaSqlServerTypes.datetimeoffset(scale))
}

object SqlServerTypes extends SqlServerTypes
