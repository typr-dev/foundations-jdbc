package dev.typr.foundations.scala

import dev.typr.foundations.{OracleType, OracleTypes => JavaOracleTypes}

/** Scala-friendly OracleType instances that use Scala types instead of Java boxed types.
  * All types from dev.typr.foundations.OracleTypes are available here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class OracleTypes {
  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val number: OracleType[BigDecimal] = JavaOracleTypes.number.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)
  val integer: OracleType[BigDecimal] = JavaOracleTypes.integer.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)
  val smallint: OracleType[BigDecimal] = JavaOracleTypes.smallint.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)

  // Primitives
  val numberInt: OracleType[Int] = JavaOracleTypes.numberInt.bimap(i => i, i => i)
  val numberLong: OracleType[Long] = JavaOracleTypes.numberLong.bimap(l => l, l => l)
  val binaryFloat: OracleType[Float] = JavaOracleTypes.binaryFloat.bimap(f => f, f => f)
  val binaryDouble: OracleType[Double] = JavaOracleTypes.binaryDouble.bimap(d => d, d => d)
  val float_ : OracleType[Double] = JavaOracleTypes.float_.bimap(d => d, d => d)
  val real: OracleType[Double] = JavaOracleTypes.real.bimap(d => d, d => d)
  val doublePrecision: OracleType[Double] = JavaOracleTypes.doublePrecision.bimap(d => d, d => d)
  val boolean_ : OracleType[Boolean] = JavaOracleTypes.boolean_.bimap(b => b, b => b)
  val numberAsBoolean: OracleType[Boolean] = JavaOracleTypes.numberAsBoolean.bimap(b => b, b => b)

  // Forward all other types directly from Java
  val varchar2 = JavaOracleTypes.varchar2
  val char_ = JavaOracleTypes.char_
  val nvarchar2 = JavaOracleTypes.nvarchar2
  val nchar = JavaOracleTypes.nchar
  val clob = JavaOracleTypes.clob
  val clobNonEmpty = JavaOracleTypes.clobNonEmpty
  val nclob = JavaOracleTypes.nclob
  val nclobNonEmpty = JavaOracleTypes.nclobNonEmpty
  val long_ = JavaOracleTypes.long_
  val raw = JavaOracleTypes.raw
  val blob = JavaOracleTypes.blob
  val blobNonEmpty = JavaOracleTypes.blobNonEmpty
  val longRaw = JavaOracleTypes.longRaw
  val date = JavaOracleTypes.date
  val timestamp = JavaOracleTypes.timestamp
  val timestampWithTimeZone = JavaOracleTypes.timestampWithTimeZone
  val timestampWithLocalTimeZone = JavaOracleTypes.timestampWithLocalTimeZone
  val intervalYearToMonth = JavaOracleTypes.intervalYearToMonth
  val intervalDayToSecond = JavaOracleTypes.intervalDayToSecond
  val rowId = JavaOracleTypes.rowId
  val uRowId = JavaOracleTypes.uRowId
  val xmlType = JavaOracleTypes.xmlType
  val json = JavaOracleTypes.json
  val unknown = JavaOracleTypes.unknown

  // Forward static methods
  def number(precision: Int): OracleType[java.math.BigDecimal] =
    JavaOracleTypes.number(precision)

  def number(precision: Int, scale: Int): OracleType[java.math.BigDecimal] =
    JavaOracleTypes.number(precision, scale)

  def numberAsInt(precision: Int): OracleType[java.lang.Integer] =
    JavaOracleTypes.numberAsInt(precision)

  def numberAsLong(precision: Int): OracleType[java.lang.Long] =
    JavaOracleTypes.numberAsLong(precision)

  def float_(binaryPrecision: Int): OracleType[java.lang.Double] =
    JavaOracleTypes.float_(binaryPrecision)

  def varchar2(maxLength: Int): OracleType[String] =
    JavaOracleTypes.varchar2(maxLength)

  def varchar2NonEmpty(maxLength: Int): OracleType[dev.typr.foundations.NonEmptyString] =
    JavaOracleTypes.varchar2NonEmpty(maxLength)

  def char_(length: Int): OracleType[String] =
    JavaOracleTypes.char_(length)

  def charPadded(length: Int): OracleType[dev.typr.foundations.PaddedString] =
    JavaOracleTypes.charPadded(length)

  def nvarchar2(maxLength: Int): OracleType[String] =
    JavaOracleTypes.nvarchar2(maxLength)

  def nvarchar2NonEmpty(maxLength: Int): OracleType[dev.typr.foundations.NonEmptyString] =
    JavaOracleTypes.nvarchar2NonEmpty(maxLength)

  def nchar(length: Int): OracleType[String] =
    JavaOracleTypes.nchar(length)

  def ncharPadded(length: Int): OracleType[dev.typr.foundations.PaddedString] =
    JavaOracleTypes.ncharPadded(length)

  def raw(maxLength: Int): OracleType[Array[Byte]] =
    JavaOracleTypes.raw(maxLength)

  def rawNonEmpty(maxLength: Int): OracleType[dev.typr.foundations.NonEmptyBlob] =
    JavaOracleTypes.rawNonEmpty(maxLength)

  def timestamp(fractionalSecondsPrecision: Int): OracleType[java.time.LocalDateTime] =
    JavaOracleTypes.timestamp(fractionalSecondsPrecision)

  def timestampWithTimeZone(fractionalSecondsPrecision: Int): OracleType[java.time.OffsetDateTime] =
    JavaOracleTypes.timestampWithTimeZone(fractionalSecondsPrecision)

  def timestampWithLocalTimeZone(fractionalSecondsPrecision: Int): OracleType[java.time.OffsetDateTime] =
    JavaOracleTypes.timestampWithLocalTimeZone(fractionalSecondsPrecision)

  def intervalYearToMonth(yearPrecision: Int): OracleType[dev.typr.foundations.data.OracleIntervalYM] =
    JavaOracleTypes.intervalYearToMonth(yearPrecision)

  def intervalDayToSecond(dayPrecision: Int, fractionalSecondsPrecision: Int): OracleType[dev.typr.foundations.data.OracleIntervalDS] =
    JavaOracleTypes.intervalDayToSecond(dayPrecision, fractionalSecondsPrecision)

  def uRowId(maxLength: Int): OracleType[String] =
    JavaOracleTypes.uRowId(maxLength)

  def ofEnum[E <: Enum[E]](sqlType: String, fromString: java.util.function.Function[String, E]): OracleType[E] =
    JavaOracleTypes.ofEnum(sqlType, fromString)
}

object OracleTypes extends OracleTypes
