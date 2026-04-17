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

  /** Alias for [[binaryFloat]] — aesthetic cross-palette naming. 4B IEEE 754. */
  val float4: OracleType[Float] = binaryFloat

  val binaryDouble: OracleType[Double] = OracleType(JavaOracleTypes.binaryDouble.transform(d => d, d => d))

  /** Alias for [[binaryDouble]] — aesthetic cross-palette naming. 8B IEEE 754. */
  val float8: OracleType[Double] = binaryDouble
  val float_ : OracleType[Double] = OracleType(JavaOracleTypes.float_.transform(d => d, d => d))
  val real: OracleType[Double] = OracleType(JavaOracleTypes.real.transform(d => d, d => d))
  val doublePrecision: OracleType[Double] = OracleType(JavaOracleTypes.doublePrecision.transform(d => d, d => d))
  val boolean_ : OracleType[Boolean] = OracleType(JavaOracleTypes.boolean_.transform(b => b, b => b))

  /** Alias for [[boolean_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val bool: OracleType[Boolean] = boolean_
  val numberAsBoolean: OracleType[Boolean] = OracleType(JavaOracleTypes.numberAsBoolean.transform(b => b, b => b))

  // Forward all other types directly from Java
  val varchar2: OracleType[String] = OracleType(JavaOracleTypes.varchar2)
  val char_ : OracleType[String] = OracleType(JavaOracleTypes.char_)

  /** Alias for [[char_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val character: OracleType[String] = char_
  val nvarchar2: OracleType[String] = OracleType(JavaOracleTypes.nvarchar2)
  val nchar: OracleType[String] = OracleType(JavaOracleTypes.nchar)
  val clob: OracleType[String] = OracleType(JavaOracleTypes.clob)
  val clobNonEmpty: OracleType[dev.typr.foundations.data.NonEmptyString] = OracleType(JavaOracleTypes.clobNonEmpty)
  val nclob: OracleType[String] = OracleType(JavaOracleTypes.nclob)
  val nclobNonEmpty: OracleType[dev.typr.foundations.data.NonEmptyString] = OracleType(JavaOracleTypes.nclobNonEmpty)
  val long_ : OracleType[String] = OracleType(JavaOracleTypes.long_)

  /** Alias for [[long_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val longColumn: OracleType[String] = long_
  val raw: OracleType[Array[Byte]] = OracleType(JavaOracleTypes.raw)
  val blob: OracleType[Array[Byte]] = OracleType(JavaOracleTypes.blob)
  val blobNonEmpty: OracleType[dev.typr.foundations.data.NonEmptyBlob] = OracleType(JavaOracleTypes.blobNonEmpty)
  val longRaw: OracleType[Array[Byte]] = OracleType(JavaOracleTypes.longRaw)
  val date: OracleType[java.time.LocalDateTime] = OracleType(JavaOracleTypes.date)
  val timestamp: OracleType[java.time.LocalDateTime] = OracleType(JavaOracleTypes.timestamp)
  val timestampWithTimeZone: OracleType[java.time.ZonedDateTime] = OracleType(JavaOracleTypes.timestampWithTimeZone)
  val timestampWithLocalTimeZone: OracleType[java.time.Instant] = OracleType(JavaOracleTypes.timestampWithLocalTimeZone)
  val intervalYearToMonth: OracleType[dev.typr.foundations.data.OracleIntervalYM] = OracleType(JavaOracleTypes.intervalYearToMonth)
  val intervalDayToSecond: OracleType[dev.typr.foundations.data.OracleIntervalDS] = OracleType(JavaOracleTypes.intervalDayToSecond)
  val rowId: OracleType[String] = OracleType(JavaOracleTypes.rowId)
  val uRowId: OracleType[String] = OracleType(JavaOracleTypes.uRowId)
  val xmlType: OracleType[String] = OracleType(JavaOracleTypes.xmlType)
  val json: OracleType[dev.typr.foundations.data.Json] = OracleType(JavaOracleTypes.json)
  val unknown: OracleType[dev.typr.foundations.data.Unknown] = OracleType(JavaOracleTypes.unknown)

  // Forward static methods with Scala type conversion
  def numberOf(precision: Int): OracleType[BigDecimal] =
    OracleType(JavaOracleTypes.numberOf(precision).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def numberOf(precision: Int, scale: Int): OracleType[BigDecimal] =
    OracleType(JavaOracleTypes.numberOf(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def numberAsInt(precision: Int): OracleType[Int] =
    OracleType(JavaOracleTypes.numberAsInt(precision).transform(i => i, i => i))

  def numberAsLong(precision: Int): OracleType[Long] =
    OracleType(JavaOracleTypes.numberAsLong(precision).transform(l => l, l => l))

  def float_Of(binaryPrecision: Int): OracleType[Double] =
    OracleType(JavaOracleTypes.float_Of(binaryPrecision).transform(d => d, d => d))

  def varchar2Of(maxLength: Int): OracleType[String] =
    OracleType(JavaOracleTypes.varchar2Of(maxLength))

  def varchar2NonEmpty(maxLength: Int): OracleType[dev.typr.foundations.data.NonEmptyString] =
    OracleType(JavaOracleTypes.varchar2NonEmpty(maxLength))

  def char_Of(length: Int): OracleType[String] =
    OracleType(JavaOracleTypes.char_Of(length))

  def charPadded(length: Int): OracleType[dev.typr.foundations.data.PaddedString] =
    OracleType(JavaOracleTypes.charPadded(length))

  def nvarchar2Of(maxLength: Int): OracleType[String] =
    OracleType(JavaOracleTypes.nvarchar2Of(maxLength))

  def nvarchar2NonEmpty(maxLength: Int): OracleType[dev.typr.foundations.data.NonEmptyString] =
    OracleType(JavaOracleTypes.nvarchar2NonEmpty(maxLength))

  def ncharOf(length: Int): OracleType[String] =
    OracleType(JavaOracleTypes.ncharOf(length))

  def ncharPadded(length: Int): OracleType[dev.typr.foundations.data.PaddedString] =
    OracleType(JavaOracleTypes.ncharPadded(length))

  def rawOf(maxLength: Int): OracleType[Array[Byte]] =
    OracleType(JavaOracleTypes.rawOf(maxLength))

  def rawNonEmpty(maxLength: Int): OracleType[dev.typr.foundations.data.NonEmptyBlob] =
    OracleType(JavaOracleTypes.rawNonEmpty(maxLength))

  def timestampOf(fractionalSecondsPrecision: Int): OracleType[java.time.LocalDateTime] =
    OracleType(JavaOracleTypes.timestampOf(fractionalSecondsPrecision))

  def timestampWithTimeZone(fractionalSecondsPrecision: Int): OracleType[java.time.ZonedDateTime] =
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
