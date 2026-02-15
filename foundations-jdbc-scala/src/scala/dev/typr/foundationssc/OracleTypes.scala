package dev.typr.foundationssc

import dev.typr.foundations.{OracleTypes => JavaOracleTypes}

/** Scala-friendly OracleType instances that use Scala types instead of Java boxed types.
  * All types from dev.typr.foundations.OracleTypes are available here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class OracleTypes {
  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val number: OracleType[BigDecimal] = OracleType(JavaOracleTypes.number.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val integer: OracleType[BigDecimal] = OracleType(JavaOracleTypes.integer.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val smallint: OracleType[BigDecimal] = OracleType(JavaOracleTypes.smallint.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Primitives
  val numberInt: OracleType[Int] = OracleType(JavaOracleTypes.numberInt.bimap(i => i, i => i))
  val numberLong: OracleType[Long] = OracleType(JavaOracleTypes.numberLong.bimap(l => l, l => l))
  val binaryFloat: OracleType[Float] = OracleType(JavaOracleTypes.binaryFloat.bimap(f => f, f => f))
  val binaryDouble: OracleType[Double] = OracleType(JavaOracleTypes.binaryDouble.bimap(d => d, d => d))
  val float_ : OracleType[Double] = OracleType(JavaOracleTypes.float_.bimap(d => d, d => d))
  val real: OracleType[Double] = OracleType(JavaOracleTypes.real.bimap(d => d, d => d))
  val doublePrecision: OracleType[Double] = OracleType(JavaOracleTypes.doublePrecision.bimap(d => d, d => d))
  val boolean_ : OracleType[Boolean] = OracleType(JavaOracleTypes.boolean_.bimap(b => b, b => b))
  val numberAsBoolean: OracleType[Boolean] = OracleType(JavaOracleTypes.numberAsBoolean.bimap(b => b, b => b))

  // Forward all other types directly from Java
  val varchar2 = OracleType(JavaOracleTypes.varchar2)
  val char_ = OracleType(JavaOracleTypes.char_)
  val nvarchar2 = OracleType(JavaOracleTypes.nvarchar2)
  val nchar = OracleType(JavaOracleTypes.nchar)
  val clob = OracleType(JavaOracleTypes.clob)
  val clobNonEmpty = OracleType(JavaOracleTypes.clobNonEmpty)
  val nclob = OracleType(JavaOracleTypes.nclob)
  val nclobNonEmpty = OracleType(JavaOracleTypes.nclobNonEmpty)
  val long_ = OracleType(JavaOracleTypes.long_)
  val raw = OracleType(JavaOracleTypes.raw)
  val blob = OracleType(JavaOracleTypes.blob)
  val blobNonEmpty = OracleType(JavaOracleTypes.blobNonEmpty)
  val longRaw = OracleType(JavaOracleTypes.longRaw)
  val date = OracleType(JavaOracleTypes.date)
  val timestamp = OracleType(JavaOracleTypes.timestamp)
  val timestampWithTimeZone = OracleType(JavaOracleTypes.timestampWithTimeZone)
  val timestampWithLocalTimeZone = OracleType(JavaOracleTypes.timestampWithLocalTimeZone)
  val intervalYearToMonth = OracleType(JavaOracleTypes.intervalYearToMonth)
  val intervalDayToSecond = OracleType(JavaOracleTypes.intervalDayToSecond)
  val rowId = OracleType(JavaOracleTypes.rowId)
  val uRowId = OracleType(JavaOracleTypes.uRowId)
  val xmlType = OracleType(JavaOracleTypes.xmlType)
  val json = OracleType(JavaOracleTypes.json)
  val unknown = OracleType(JavaOracleTypes.unknown)

  // Forward static methods with Scala type conversion
  def number(precision: Int): OracleType[BigDecimal] =
    OracleType(JavaOracleTypes.number(precision).bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def number(precision: Int, scale: Int): OracleType[BigDecimal] =
    OracleType(JavaOracleTypes.number(precision, scale).bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def numberAsInt(precision: Int): OracleType[Int] =
    OracleType(JavaOracleTypes.numberAsInt(precision).bimap(i => i, i => i))

  def numberAsLong(precision: Int): OracleType[Long] =
    OracleType(JavaOracleTypes.numberAsLong(precision).bimap(l => l, l => l))

  def float_(binaryPrecision: Int): OracleType[Double] =
    OracleType(JavaOracleTypes.float_(binaryPrecision).bimap(d => d, d => d))

  def varchar2(maxLength: Int): OracleType[String] =
    OracleType(JavaOracleTypes.varchar2(maxLength))

  def varchar2NonEmpty(maxLength: Int): OracleType[dev.typr.foundations.data.NonEmptyString] =
    OracleType(JavaOracleTypes.varchar2NonEmpty(maxLength))

  def char_(length: Int): OracleType[String] =
    OracleType(JavaOracleTypes.char_(length))

  def charPadded(length: Int): OracleType[dev.typr.foundations.data.PaddedString] =
    OracleType(JavaOracleTypes.charPadded(length))

  def nvarchar2(maxLength: Int): OracleType[String] =
    OracleType(JavaOracleTypes.nvarchar2(maxLength))

  def nvarchar2NonEmpty(maxLength: Int): OracleType[dev.typr.foundations.data.NonEmptyString] =
    OracleType(JavaOracleTypes.nvarchar2NonEmpty(maxLength))

  def nchar(length: Int): OracleType[String] =
    OracleType(JavaOracleTypes.nchar(length))

  def ncharPadded(length: Int): OracleType[dev.typr.foundations.data.PaddedString] =
    OracleType(JavaOracleTypes.ncharPadded(length))

  def raw(maxLength: Int): OracleType[Array[Byte]] =
    OracleType(JavaOracleTypes.raw(maxLength))

  def rawNonEmpty(maxLength: Int): OracleType[dev.typr.foundations.data.NonEmptyBlob] =
    OracleType(JavaOracleTypes.rawNonEmpty(maxLength))

  def timestamp(fractionalSecondsPrecision: Int): OracleType[java.time.LocalDateTime] =
    OracleType(JavaOracleTypes.timestamp(fractionalSecondsPrecision))

  def timestampWithTimeZone(fractionalSecondsPrecision: Int): OracleType[java.time.OffsetDateTime] =
    OracleType(JavaOracleTypes.timestampWithTimeZone(fractionalSecondsPrecision))

  def timestampWithLocalTimeZone(fractionalSecondsPrecision: Int): OracleType[java.time.OffsetDateTime] =
    OracleType(JavaOracleTypes.timestampWithLocalTimeZone(fractionalSecondsPrecision))

  def intervalYearToMonth(yearPrecision: Int): OracleType[dev.typr.foundations.data.OracleIntervalYM] =
    OracleType(JavaOracleTypes.intervalYearToMonth(yearPrecision))

  def intervalDayToSecond(dayPrecision: Int, fractionalSecondsPrecision: Int): OracleType[dev.typr.foundations.data.OracleIntervalDS] =
    OracleType(JavaOracleTypes.intervalDayToSecond(dayPrecision, fractionalSecondsPrecision))

  def uRowId(maxLength: Int): OracleType[String] =
    OracleType(JavaOracleTypes.uRowId(maxLength))

  def ofEnum[E <: Enum[E]](sqlType: String, fromString: java.util.function.Function[String, E]): OracleType[E] =
    OracleType(JavaOracleTypes.ofEnum(sqlType, fromString))
}

object OracleTypes extends OracleTypes
