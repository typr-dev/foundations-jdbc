package dev.typr.foundations.scala

import dev.typr.foundations.{DuckDbType, DuckDbTypes => JavaDuckDbTypes}

/** Scala-friendly DuckDbType instances that use Scala types instead of Java boxed types.
  * All types from dev.typr.foundations.DuckDbTypes are available here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class DuckDbTypes {
  // Primitives - convert Java boxed types to Scala native types
  val tinyint: DuckDbType[Byte] = JavaDuckDbTypes.tinyint.bimap(b => b, b => b)
  val smallint: DuckDbType[Short] = JavaDuckDbTypes.smallint.bimap(s => s, s => s)
  val integer: DuckDbType[Int] = JavaDuckDbTypes.integer.bimap(i => i, i => i)
  val bigint: DuckDbType[Long] = JavaDuckDbTypes.bigint.bimap(l => l, l => l)
  val float_ : DuckDbType[Float] = JavaDuckDbTypes.float_.bimap(f => f, f => f)
  val double_ : DuckDbType[Double] = JavaDuckDbTypes.double_.bimap(d => d, d => d)
  val boolean_ : DuckDbType[Boolean] = JavaDuckDbTypes.boolean_.bimap(b => b, b => b)
  val bool: DuckDbType[Boolean] = JavaDuckDbTypes.bool.bimap(b => b, b => b)

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: DuckDbType[BigDecimal] = JavaDuckDbTypes.decimal.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)
  val numeric: DuckDbType[BigDecimal] = JavaDuckDbTypes.numeric.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)

  // Array types - convert Java boxed arrays to Scala native arrays
  val tinyintArray: DuckDbType[Array[Byte]] = JavaDuckDbTypes.tinyintArray.bimap(
    arr => arr.map(_.byteValue()),
    arr => arr.map(java.lang.Byte.valueOf)
  )
  val smallintArray: DuckDbType[Array[Short]] = JavaDuckDbTypes.smallintArray.bimap(
    arr => arr.map(_.shortValue()),
    arr => arr.map(java.lang.Short.valueOf)
  )
  val integerArray: DuckDbType[Array[Int]] = JavaDuckDbTypes.integerArray.bimap(
    arr => arr.map(_.intValue()),
    arr => arr.map(java.lang.Integer.valueOf)
  )
  val bigintArray: DuckDbType[Array[Long]] = JavaDuckDbTypes.bigintArray.bimap(
    arr => arr.map(_.longValue()),
    arr => arr.map(java.lang.Long.valueOf)
  )
  val floatArray: DuckDbType[Array[Float]] = JavaDuckDbTypes.floatArray.bimap(
    arr => arr.map(_.floatValue()),
    arr => arr.map(java.lang.Float.valueOf)
  )
  val doubleArray: DuckDbType[Array[Double]] = JavaDuckDbTypes.doubleArray.bimap(
    arr => arr.map(_.doubleValue()),
    arr => arr.map(java.lang.Double.valueOf)
  )
  val booleanArray: DuckDbType[Array[Boolean]] = JavaDuckDbTypes.booleanArray.bimap(
    arr => arr.map(_.booleanValue()),
    arr => arr.map(java.lang.Boolean.valueOf)
  )
  val decimalArray: DuckDbType[Array[BigDecimal]] = JavaDuckDbTypes.decimalArray.bimap(
    arr => arr.map(BigDecimal(_)),
    arr => arr.map(_.bigDecimal)
  )

  // Forward all other types directly from Java
  val hugeint = JavaDuckDbTypes.hugeint
  val utinyint = JavaDuckDbTypes.utinyint
  val usmallint = JavaDuckDbTypes.usmallint
  val uinteger = JavaDuckDbTypes.uinteger
  val ubigint = JavaDuckDbTypes.ubigint
  val uhugeint = JavaDuckDbTypes.uhugeint
  val real = JavaDuckDbTypes.real
  val float4 = JavaDuckDbTypes.float4
  val float8 = JavaDuckDbTypes.float8
  val varchar = JavaDuckDbTypes.varchar
  val text = JavaDuckDbTypes.text
  val string = JavaDuckDbTypes.string
  val char_ = JavaDuckDbTypes.char_
  val bpchar = JavaDuckDbTypes.bpchar
  val blob = JavaDuckDbTypes.blob
  val bytea = JavaDuckDbTypes.bytea
  val binary = JavaDuckDbTypes.binary
  val varbinary = JavaDuckDbTypes.varbinary
  val bit = JavaDuckDbTypes.bit
  val bitstring = JavaDuckDbTypes.bitstring
  val date = JavaDuckDbTypes.date
  val time = JavaDuckDbTypes.time
  val timestamp = JavaDuckDbTypes.timestamp
  val datetime = JavaDuckDbTypes.datetime
  val timestamptz = JavaDuckDbTypes.timestamptz
  val timetz = JavaDuckDbTypes.timetz
  val timestamp_s = JavaDuckDbTypes.timestamp_s
  val timestamp_ms = JavaDuckDbTypes.timestamp_ms
  val timestamp_ns = JavaDuckDbTypes.timestamp_ns
  val interval = JavaDuckDbTypes.interval
  val uuid = JavaDuckDbTypes.uuid
  val json = JavaDuckDbTypes.json
  val hugeintArray = JavaDuckDbTypes.hugeintArray
  val utinyintArray = JavaDuckDbTypes.utinyintArray
  val usmallintArray = JavaDuckDbTypes.usmallintArray
  val uintegerArray = JavaDuckDbTypes.uintegerArray
  val ubigintArray = JavaDuckDbTypes.ubigintArray
  val varcharArray = JavaDuckDbTypes.varcharArray
  val blobArray = JavaDuckDbTypes.blobArray
  val dateArray = JavaDuckDbTypes.dateArray
  val timeArray = JavaDuckDbTypes.timeArray
  val timestampArray = JavaDuckDbTypes.timestampArray
  val timestamptzArray = JavaDuckDbTypes.timestamptzArray
  val intervalArray = JavaDuckDbTypes.intervalArray
  val uuidArray = JavaDuckDbTypes.uuidArray
  val jsonArray = JavaDuckDbTypes.jsonArray
  val listBoolean = JavaDuckDbTypes.listBoolean
  val listTinyint = JavaDuckDbTypes.listTinyint
  val listSmallint = JavaDuckDbTypes.listSmallint
  val listInteger = JavaDuckDbTypes.listInteger
  val listBigint = JavaDuckDbTypes.listBigint
  val listFloat = JavaDuckDbTypes.listFloat
  val listDouble = JavaDuckDbTypes.listDouble
  val listVarchar = JavaDuckDbTypes.listVarchar
  val listUuid = JavaDuckDbTypes.listUuid
  val listDate = JavaDuckDbTypes.listDate
  val listTime = JavaDuckDbTypes.listTime
  val listTimestamp = JavaDuckDbTypes.listTimestamp
  val listTimestamptz = JavaDuckDbTypes.listTimestamptz
  val listDecimal = JavaDuckDbTypes.listDecimal
  val listHugeint = JavaDuckDbTypes.listHugeint
  val listInterval = JavaDuckDbTypes.listInterval
  val unknown = JavaDuckDbTypes.unknown

  // Forward static methods
  def decimal(precision: Int, scale: Int): DuckDbType[java.math.BigDecimal] =
    JavaDuckDbTypes.decimal(precision, scale)

  def varchar(length: Int): DuckDbType[String] =
    JavaDuckDbTypes.varchar(length)

  def char_(length: Int): DuckDbType[String] =
    JavaDuckDbTypes.char_(length)

  def bit(length: Int): DuckDbType[String] =
    JavaDuckDbTypes.bit(length)

  def ofEnum[E <: Enum[E]](enumTypeName: String, fromString: java.util.function.Function[String, E]): DuckDbType[E] =
    JavaDuckDbTypes.ofEnum(enumTypeName, fromString)
}

object DuckDbTypes extends DuckDbTypes
