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

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: DuckDbType[BigDecimal] = DuckDbType(JavaDuckDbTypes.decimal.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val numeric: DuckDbType[BigDecimal] = DuckDbType(JavaDuckDbTypes.numeric.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Array types - convert Java boxed arrays to Scala native arrays
  val tinyintArray: DuckDbType[Array[Byte]] = DuckDbType(
    JavaDuckDbTypes.tinyintArray.transform(
      arr => arr.map(_.byteValue()),
      arr => arr.map(java.lang.Byte.valueOf)
    )
  )
  val smallintArray: DuckDbType[Array[Short]] = DuckDbType(
    JavaDuckDbTypes.smallintArray.transform(
      arr => arr.map(_.shortValue()),
      arr => arr.map(java.lang.Short.valueOf)
    )
  )
  val integerArray: DuckDbType[Array[Int]] = DuckDbType(
    JavaDuckDbTypes.integerArray.transform(
      arr => arr.map(_.intValue()),
      arr => arr.map(java.lang.Integer.valueOf)
    )
  )
  val bigintArray: DuckDbType[Array[Long]] = DuckDbType(
    JavaDuckDbTypes.bigintArray.transform(
      arr => arr.map(_.longValue()),
      arr => arr.map(java.lang.Long.valueOf)
    )
  )
  val floatArray: DuckDbType[Array[Float]] = DuckDbType(
    JavaDuckDbTypes.floatArray.transform(
      arr => arr.map(_.floatValue()),
      arr => arr.map(java.lang.Float.valueOf)
    )
  )
  val doubleArray: DuckDbType[Array[Double]] = DuckDbType(
    JavaDuckDbTypes.doubleArray.transform(
      arr => arr.map(_.doubleValue()),
      arr => arr.map(java.lang.Double.valueOf)
    )
  )
  val booleanArray: DuckDbType[Array[Boolean]] = DuckDbType(
    JavaDuckDbTypes.booleanArray.transform(
      arr => arr.map(_.booleanValue()),
      arr => arr.map(java.lang.Boolean.valueOf)
    )
  )
  val decimalArray: DuckDbType[Array[BigDecimal]] = DuckDbType(
    JavaDuckDbTypes.decimalArray.transform(
      arr => arr.map(BigDecimal(_)),
      arr => arr.map(_.bigDecimal)
    )
  )

  // Forward all other types directly from Java
  val hugeint = DuckDbType(JavaDuckDbTypes.hugeint)
  val utinyint = DuckDbType(JavaDuckDbTypes.utinyint)
  val usmallint = DuckDbType(JavaDuckDbTypes.usmallint)
  val uinteger = DuckDbType(JavaDuckDbTypes.uinteger)
  val ubigint = DuckDbType(JavaDuckDbTypes.ubigint)
  val uhugeint = DuckDbType(JavaDuckDbTypes.uhugeint)
  val real = DuckDbType(JavaDuckDbTypes.real)
  val float4 = DuckDbType(JavaDuckDbTypes.float4)
  val float8 = DuckDbType(JavaDuckDbTypes.float8)
  val varchar = DuckDbType(JavaDuckDbTypes.varchar)
  val text = DuckDbType(JavaDuckDbTypes.text)
  val string = DuckDbType(JavaDuckDbTypes.string)
  val char_ = DuckDbType(JavaDuckDbTypes.char_)
  val bpchar = DuckDbType(JavaDuckDbTypes.bpchar)
  val blob = DuckDbType(JavaDuckDbTypes.blob)
  val bytea = DuckDbType(JavaDuckDbTypes.bytea)
  val binary = DuckDbType(JavaDuckDbTypes.binary)
  val varbinary = DuckDbType(JavaDuckDbTypes.varbinary)
  val bit = DuckDbType(JavaDuckDbTypes.bit)
  val bitstring = DuckDbType(JavaDuckDbTypes.bitstring)
  val date = DuckDbType(JavaDuckDbTypes.date)
  val time = DuckDbType(JavaDuckDbTypes.time)
  val timestamp = DuckDbType(JavaDuckDbTypes.timestamp)
  val datetime = DuckDbType(JavaDuckDbTypes.datetime)
  val timestamptz = DuckDbType(JavaDuckDbTypes.timestamptz)
  val timetz = DuckDbType(JavaDuckDbTypes.timetz)
  val timestamp_s = DuckDbType(JavaDuckDbTypes.timestamp_s)
  val timestamp_ms = DuckDbType(JavaDuckDbTypes.timestamp_ms)
  val timestamp_ns = DuckDbType(JavaDuckDbTypes.timestamp_ns)
  val interval = DuckDbType(JavaDuckDbTypes.interval)
  val uuid = DuckDbType(JavaDuckDbTypes.uuid)
  val json = DuckDbType(JavaDuckDbTypes.json)
  val hugeintArray = DuckDbType(JavaDuckDbTypes.hugeintArray)
  val utinyintArray = DuckDbType(JavaDuckDbTypes.utinyintArray)
  val usmallintArray = DuckDbType(JavaDuckDbTypes.usmallintArray)
  val uintegerArray = DuckDbType(JavaDuckDbTypes.uintegerArray)
  val ubigintArray = DuckDbType(JavaDuckDbTypes.ubigintArray)
  val varcharArray = DuckDbType(JavaDuckDbTypes.varcharArray)
  // blobArray removed: BLOB[] not supported (binary can't be serialized in DuckDBUserArray)
  val dateArray = DuckDbType(JavaDuckDbTypes.dateArray)
  val timeArray = DuckDbType(JavaDuckDbTypes.timeArray)
  val timestampArray = DuckDbType(JavaDuckDbTypes.timestampArray)
  val timestamptzArray = DuckDbType(JavaDuckDbTypes.timestamptzArray)
  val intervalArray = DuckDbType(JavaDuckDbTypes.intervalArray)
  val uuidArray = DuckDbType(JavaDuckDbTypes.uuidArray)
  val jsonArray = DuckDbType(JavaDuckDbTypes.jsonArray)
  val unknown = DuckDbType(JavaDuckDbTypes.unknown)

  // Forward static methods with Scala type conversion
  def decimal(precision: Int, scale: Int): DuckDbType[BigDecimal] =
    DuckDbType(JavaDuckDbTypes.decimal(precision, scale).transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def varchar(length: Int): DuckDbType[String] =
    DuckDbType(JavaDuckDbTypes.varchar(length))

  def char_(length: Int): DuckDbType[String] =
    DuckDbType(JavaDuckDbTypes.char_(length))

  def bit(length: Int): DuckDbType[String] =
    DuckDbType(JavaDuckDbTypes.bit(length))

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
