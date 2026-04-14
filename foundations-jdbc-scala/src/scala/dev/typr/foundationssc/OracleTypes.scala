package dev.typr.foundationssc

import dev.typr.foundations.{OracleTypes => JavaOracleTypes}

/** Scala-friendly OracleType instances that use Scala types instead of Java boxed types. All types from dev.typr.foundations.OracleTypes are available here,
  * with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class OracleTypes {
  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val number: OracleType[BigDecimal] = OracleType(JavaOracleTypes.number.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val integer: OracleType[BigDecimal] = OracleType(JavaOracleTypes.integer.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val smallint: OracleType[BigDecimal] = OracleType(JavaOracleTypes.smallint.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Primitives
  val numberInt: OracleType[Int] = OracleType(JavaOracleTypes.numberInt.transform(i => i, i => i))
  val numberLong: OracleType[Long] = OracleType(JavaOracleTypes.numberLong.transform(l => l, l => l))
  val binaryFloat: OracleType[Float] = OracleType(JavaOracleTypes.binaryFloat.transform(f => f, f => f))
  val binaryDouble: OracleType[Double] = OracleType(JavaOracleTypes.binaryDouble.transform(d => d, d => d))
  val float_ : OracleType[Double] = OracleType(JavaOracleTypes.float_.transform(d => d, d => d))
  val real: OracleType[Double] = OracleType(JavaOracleTypes.real.transform(d => d, d => d))
  val doublePrecision: OracleType[Double] = OracleType(JavaOracleTypes.doublePrecision.transform(d => d, d => d))
  val boolean_ : OracleType[Boolean] = OracleType(JavaOracleTypes.boolean_.transform(b => b, b => b))
  val numberAsBoolean: OracleType[Boolean] = OracleType(JavaOracleTypes.numberAsBoolean.transform(b => b, b => b))

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
    OracleType(JavaOracleTypes.number(precision).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def number(precision: Int, scale: Int): OracleType[BigDecimal] =
    OracleType(JavaOracleTypes.number(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def numberAsInt(precision: Int): OracleType[Int] =
    OracleType(JavaOracleTypes.numberAsInt(precision).transform(i => i, i => i))

  def numberAsLong(precision: Int): OracleType[Long] =
    OracleType(JavaOracleTypes.numberAsLong(precision).transform(l => l, l => l))

  def float_(binaryPrecision: Int): OracleType[Double] =
    OracleType(JavaOracleTypes.float_(binaryPrecision).transform(d => d, d => d))

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

  def timestampWithLocalTimeZone(fractionalSecondsPrecision: Int): OracleType[java.time.Instant] =
    OracleType(JavaOracleTypes.timestampWithLocalTimeZone(fractionalSecondsPrecision))

  def intervalYearToMonth(yearPrecision: Int): OracleType[dev.typr.foundations.data.OracleIntervalYM] =
    OracleType(JavaOracleTypes.intervalYearToMonth(yearPrecision))

  def intervalDayToSecond(dayPrecision: Int, fractionalSecondsPrecision: Int): OracleType[dev.typr.foundations.data.OracleIntervalDS] =
    OracleType(JavaOracleTypes.intervalDayToSecond(dayPrecision, fractionalSecondsPrecision))

  def uRowId(maxLength: Int): OracleType[String] =
    OracleType(JavaOracleTypes.uRowId(maxLength))

  def ofEnum[E <: Enum[E]](sqlType: String, fromString: java.util.function.Function[String, E]): OracleType[E] =
    OracleType(JavaOracleTypes.ofEnum(sqlType, fromString))

  // JSON-encoded row types

  def jsonArrayEncoded[Row](parser: RowCodec[Row]): OracleType[Row] =
    OracleType(JavaOracleTypes.jsonArrayEncoded(parser.underlying))

  def jsonArrayEncodedList[Row](parser: RowCodec[Row]): OracleType[List[Row]] =
    OracleType(
      JavaOracleTypes
        .jsonArrayEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )

  def jsonObjectEncoded[Row](parser: RowCodecNamed[Row]): OracleType[Row] =
    OracleType(JavaOracleTypes.jsonObjectEncoded(parser.underlying))

  def jsonObjectEncodedList[Row](parser: RowCodecNamed[Row]): OracleType[List[Row]] =
    OracleType(
      JavaOracleTypes
        .jsonObjectEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )

  /** Build a named Oracle OBJECT type from a RowCodecNamed. */
  def compositeOf[Row](objectTypeName: String, codec: RowCodecNamed[Row]): OracleType[Row] =
    OracleType(JavaOracleTypes.compositeOf(objectTypeName, codec.underlying))
}

object OracleTypes extends OracleTypes
