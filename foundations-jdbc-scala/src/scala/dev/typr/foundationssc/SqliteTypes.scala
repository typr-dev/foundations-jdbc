package dev.typr.foundationssc

import dev.typr.foundations.{SqliteTypes => JavaSqliteTypes}
import dev.typr.foundations.data.{Json, Unknown}

/** Scala-friendly SqliteType instances that use Scala types instead of Java boxed types. All types
  * from dev.typr.foundations.SqliteTypes are available here, with primitives and BigDecimal
  * converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class SqliteTypes:
  // INTEGER affinity
  val integer: SqliteType[Long] = SqliteType(JavaSqliteTypes.integer.transform(l => l, l => l))
  val bigint: SqliteType[Long] = SqliteType(JavaSqliteTypes.bigint.transform(l => l, l => l))
  val int_ : SqliteType[Int] = SqliteType(JavaSqliteTypes.int_.transform(i => i, i => i))
  val smallint: SqliteType[Short] = SqliteType(JavaSqliteTypes.smallint.transform(s => s, s => s))
  val tinyint: SqliteType[Byte] = SqliteType(JavaSqliteTypes.tinyint.transform(b => b, b => b))
  val boolean_ : SqliteType[Boolean] = SqliteType(JavaSqliteTypes.boolean_.transform(b => b, b => b))

  /** Alias for [[boolean_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val bool: SqliteType[Boolean] = boolean_

  // REAL affinity
  val real: SqliteType[Double] = SqliteType(JavaSqliteTypes.real.transform(d => d, d => d))
  val double_ : SqliteType[Double] = SqliteType(JavaSqliteTypes.double_.transform(d => d, d => d))
  val doublePrecision: SqliteType[Double] = SqliteType(JavaSqliteTypes.doublePrecision.transform(d => d, d => d))
  val float_ : SqliteType[Float] = SqliteType(JavaSqliteTypes.float_.transform(f => f, f => f))

  // NUMERIC — convert Java BigDecimal to Scala BigDecimal
  val numeric: SqliteType[BigDecimal] =
    SqliteType(JavaSqliteTypes.numeric.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val decimal: SqliteType[BigDecimal] =
    SqliteType(JavaSqliteTypes.decimal.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // TEXT
  val text: SqliteType[String] = SqliteType(JavaSqliteTypes.text)
  val varchar: SqliteType[String] = SqliteType(JavaSqliteTypes.varchar)
  val char_ : SqliteType[String] = SqliteType(JavaSqliteTypes.char_)
  val clob: SqliteType[String] = SqliteType(JavaSqliteTypes.clob)

  // BLOB
  val blob: SqliteType[Array[Byte]] = SqliteType(JavaSqliteTypes.blob)
  val binary: SqliteType[Array[Byte]] = SqliteType(JavaSqliteTypes.binary)
  val varbinary: SqliteType[Array[Byte]] = SqliteType(JavaSqliteTypes.varbinary)

  // Date/Time (TEXT, ISO-8601)
  val date: SqliteType[java.time.LocalDate] = SqliteType(JavaSqliteTypes.date)
  val time: SqliteType[java.time.LocalTime] = SqliteType(JavaSqliteTypes.time)
  val datetime: SqliteType[java.time.LocalDateTime] = SqliteType(JavaSqliteTypes.datetime)
  val timestamp: SqliteType[java.time.LocalDateTime] = SqliteType(JavaSqliteTypes.timestamp)
  val instant: SqliteType[java.time.Instant] = SqliteType(JavaSqliteTypes.instant)

  // TEXT-backed convenience types
  val uuid: SqliteType[java.util.UUID] = SqliteType(JavaSqliteTypes.uuid)
  val json: SqliteType[Json] = SqliteType(JavaSqliteTypes.json)
  val unknown: SqliteType[Unknown] = SqliteType(JavaSqliteTypes.unknown)

  // Parameterized
  def decimalOf(precision: Int, scale: Int): SqliteType[BigDecimal] =
    SqliteType(JavaSqliteTypes.decimalOf(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def varcharOf(length: Int): SqliteType[String] = SqliteType(JavaSqliteTypes.varcharOf(length))

  def charOf(length: Int): SqliteType[String] = SqliteType(JavaSqliteTypes.charOf(length))

  def ofEnum[E <: Enum[E]](fromString: java.util.function.Function[String, E]): SqliteType[E] =
    SqliteType(JavaSqliteTypes.ofEnum(fromString))

  def ofEnum[E](values: Array[E]): SqliteType[E] =
    ofEnum(values, ((e: E) => e.toString): java.util.function.Function[E, String])

  def ofEnum[E](values: Array[E], name: java.util.function.Function[E, String]): SqliteType[E] =
    SqliteType(JavaSqliteTypes.ofEnum(values.asInstanceOf[Array[Object & E]], name))

object SqliteTypes extends SqliteTypes
