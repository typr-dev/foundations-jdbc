package dev.typr.foundationssc

import dev.typr.foundations.{DuckDbTypes => JavaDuckDbTypes}

/** Scala-friendly DuckDbType instances that use Scala types instead of Java boxed types. All types from dev.typr.foundations.DuckDbTypes are available here,
  * with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class DuckDbTypes {
  // Primitives - convert Java boxed types to Scala native types
  val tinyint: DuckDbType[Byte] = DuckDbType(JavaDuckDbTypes.tinyint.transform(b => b, b => b))
  val smallint: DuckDbType[Short] = DuckDbType(JavaDuckDbTypes.smallint.transform(s => s, s => s))
  val integer: DuckDbType[Int] = DuckDbType(JavaDuckDbTypes.integer.transform(i => i, i => i))
  val bigint: DuckDbType[Long] = DuckDbType(JavaDuckDbTypes.bigint.transform(l => l, l => l))
  val float_ : DuckDbType[Float] = DuckDbType(JavaDuckDbTypes.float_.transform(f => f, f => f))
  val double_ : DuckDbType[Double] = DuckDbType(JavaDuckDbTypes.double_.transform(d => d, d => d))
  val boolean_ : DuckDbType[Boolean] = DuckDbType(JavaDuckDbTypes.boolean_.transform(b => b, b => b))

  /** Alias for [[boolean_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val bool: DuckDbType[Boolean] = boolean_

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: DuckDbType[BigDecimal] = DuckDbType(JavaDuckDbTypes.decimal.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val numeric: DuckDbType[BigDecimal] = DuckDbType(JavaDuckDbTypes.numeric.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Pre-defined boxed-array values removed; users call `.list` or `.array(n)` on the scalar type
  // to obtain a DuckDbType[List[T]].

  // Forward all other types directly from Java
  val hugeint: DuckDbType[java.math.BigInteger] = DuckDbType(JavaDuckDbTypes.hugeint)
  val utinyint: DuckDbType[dev.typr.foundations.data.Uint1] = DuckDbType(JavaDuckDbTypes.utinyint)
  val usmallint: DuckDbType[dev.typr.foundations.data.Uint2] = DuckDbType(JavaDuckDbTypes.usmallint)
  val uinteger: DuckDbType[dev.typr.foundations.data.Uint4] = DuckDbType(JavaDuckDbTypes.uinteger)
  val ubigint: DuckDbType[dev.typr.foundations.data.Uint8] = DuckDbType(JavaDuckDbTypes.ubigint)
  val uhugeint: DuckDbType[java.math.BigInteger] = DuckDbType(JavaDuckDbTypes.uhugeint)
  val real: DuckDbType[java.lang.Float] = DuckDbType(JavaDuckDbTypes.real)
  val float4: DuckDbType[java.lang.Float] = DuckDbType(JavaDuckDbTypes.float4)
  val float8: DuckDbType[java.lang.Double] = DuckDbType(JavaDuckDbTypes.float8)
  val varchar: DuckDbType[String] = DuckDbType(JavaDuckDbTypes.varchar)
  val text: DuckDbType[String] = DuckDbType(JavaDuckDbTypes.text)
  val string: DuckDbType[String] = DuckDbType(JavaDuckDbTypes.string)
  val char_ : DuckDbType[String] = DuckDbType(JavaDuckDbTypes.char_)

  /** Alias for [[char_]] — aesthetic, avoids the Java-keyword `_` suffix. */
  val character: DuckDbType[String] = char_
  val bpchar: DuckDbType[String] = DuckDbType(JavaDuckDbTypes.bpchar)
  val blob: DuckDbType[Array[Byte]] = DuckDbType(JavaDuckDbTypes.blob)
  val bytea: DuckDbType[Array[Byte]] = DuckDbType(JavaDuckDbTypes.bytea)
  val binary: DuckDbType[Array[Byte]] = DuckDbType(JavaDuckDbTypes.binary)
  val varbinary: DuckDbType[Array[Byte]] = DuckDbType(JavaDuckDbTypes.varbinary)
  val bit: DuckDbType[String] = DuckDbType(JavaDuckDbTypes.bit)
  val bitstring: DuckDbType[String] = DuckDbType(JavaDuckDbTypes.bitstring)
  val date: DuckDbType[java.time.LocalDate] = DuckDbType(JavaDuckDbTypes.date)
  val time: DuckDbType[java.time.LocalTime] = DuckDbType(JavaDuckDbTypes.time)
  val timestamp: DuckDbType[java.time.LocalDateTime] = DuckDbType(JavaDuckDbTypes.timestamp)
  val datetime: DuckDbType[java.time.LocalDateTime] = DuckDbType(JavaDuckDbTypes.datetime)
  val timestamptz: DuckDbType[java.time.Instant] = DuckDbType(JavaDuckDbTypes.timestamptz)
  val timetz: DuckDbType[java.time.OffsetDateTime] = DuckDbType(JavaDuckDbTypes.timetz)
  val timestamp_s: DuckDbType[java.time.LocalDateTime] = DuckDbType(JavaDuckDbTypes.timestamp_s)
  val timestamp_ms: DuckDbType[java.time.LocalDateTime] = DuckDbType(JavaDuckDbTypes.timestamp_ms)
  val timestamp_ns: DuckDbType[java.time.LocalDateTime] = DuckDbType(JavaDuckDbTypes.timestamp_ns)
  val interval: DuckDbType[java.time.Duration] = DuckDbType(JavaDuckDbTypes.interval)
  val uuid: DuckDbType[java.util.UUID] = DuckDbType(JavaDuckDbTypes.uuid)
  val json: DuckDbType[dev.typr.foundations.data.Json] = DuckDbType(JavaDuckDbTypes.json)
  // Pre-defined array-of-T values removed; users call `.list` or `.array(n)` on the scalar type.
  val unknown: DuckDbType[dev.typr.foundations.data.Unknown] = DuckDbType(JavaDuckDbTypes.unknown)

  // Forward static methods with Scala type conversion
  def decimalOf(precision: Int, scale: Int): DuckDbType[BigDecimal] =
    DuckDbType(JavaDuckDbTypes.decimalOf(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def varcharOf(length: Int): DuckDbType[String] =
    DuckDbType(JavaDuckDbTypes.varcharOf(length))

  def char_Of(length: Int): DuckDbType[String] =
    DuckDbType(JavaDuckDbTypes.char_Of(length))

  def bitOf(length: Int): DuckDbType[String] =
    DuckDbType(JavaDuckDbTypes.bitOf(length))

  def ofEnum[E <: Enum[E]](enumTypeName: String, fromString: java.util.function.Function[String, E]): DuckDbType[E] =
    DuckDbType(JavaDuckDbTypes.ofEnum(enumTypeName, fromString))

  // Composite (STRUCT) types

  def compositeOf[Row](structName: String, codec: RowCodecNamed[Row]): DuckDbType[Row] =
    DuckDbType(JavaDuckDbTypes.compositeOf(structName, codec.underlying))

  // JSON-encoded row types

  /** A JSON column type that stores a single row as a positional JSON array: [val1, val2, val3]. */
  def jsonArrayEncoded[Row](parser: RowCodec[Row]): DuckDbType[Row] =
    DuckDbType(JavaDuckDbTypes.jsonArrayEncoded(parser.underlying))

  /** A JSON column type that stores a list of rows, each as a positional JSON array. */
  def jsonArrayEncodedList[Row](parser: RowCodec[Row]): DuckDbType[List[Row]] =
    DuckDbType(
      JavaDuckDbTypes
        .jsonArrayEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )

  /** A JSON column type that stores a single row as a keyed JSON object: {"col": val, ...}. */
  def jsonObjectEncoded[Row](parser: RowCodecNamed[Row]): DuckDbType[Row] =
    DuckDbType(JavaDuckDbTypes.jsonObjectEncoded(parser.underlying))

  /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
  def jsonObjectEncodedList[Row](parser: RowCodecNamed[Row]): DuckDbType[List[Row]] =
    DuckDbType(
      JavaDuckDbTypes
        .jsonObjectEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )

  /** JSON codec for Map[K, V] that serializes as a JSON object. */
  def mapJson[K, V](
      keyJson: dev.typr.foundations.DuckDbJson[K],
      valueJson: dev.typr.foundations.DuckDbJson[V]
  ): dev.typr.foundations.DuckDbJson[Map[K, V]] =
    JavaDuckDbTypes
      .mapJson(keyJson, valueJson)
      .transform(
        jmap => scala.jdk.CollectionConverters.MapHasAsScala(jmap).asScala.toMap,
        smap => java.util.Map.copyOf(scala.jdk.CollectionConverters.MapHasAsJava(smap).asJava)
      )
}

object DuckDbTypes extends DuckDbTypes
