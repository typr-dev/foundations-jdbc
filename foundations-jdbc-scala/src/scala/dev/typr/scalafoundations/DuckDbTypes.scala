package dev.typr.scalafoundations

import dev.typr.foundations.{DuckDbTypes => JavaDuckDbTypes}

/** Scala-friendly DuckDbType instances that use Scala types instead of Java boxed types.
  * All types from dev.typr.foundations.DuckDbTypes are available here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class DuckDbTypes {
  // Primitives - convert Java boxed types to Scala native types
  val tinyint: DuckDbType[Byte] = DuckDbType(JavaDuckDbTypes.tinyint.bimap(b => b, b => b))
  val smallint: DuckDbType[Short] = DuckDbType(JavaDuckDbTypes.smallint.bimap(s => s, s => s))
  val integer: DuckDbType[Int] = DuckDbType(JavaDuckDbTypes.integer.bimap(i => i, i => i))
  val bigint: DuckDbType[Long] = DuckDbType(JavaDuckDbTypes.bigint.bimap(l => l, l => l))
  val float_ : DuckDbType[Float] = DuckDbType(JavaDuckDbTypes.float_.bimap(f => f, f => f))
  val double_ : DuckDbType[Double] = DuckDbType(JavaDuckDbTypes.double_.bimap(d => d, d => d))
  val boolean_ : DuckDbType[Boolean] = DuckDbType(JavaDuckDbTypes.boolean_.bimap(b => b, b => b))


  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val decimal: DuckDbType[BigDecimal] = DuckDbType(JavaDuckDbTypes.decimal.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))
  val numeric: DuckDbType[BigDecimal] = DuckDbType(JavaDuckDbTypes.numeric.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Array types - convert Java boxed arrays to Scala native arrays
  val tinyintArray: DuckDbType[Array[Byte]] = DuckDbType(JavaDuckDbTypes.tinyintArray.bimap(
    arr => arr.map(_.byteValue()),
    arr => arr.map(java.lang.Byte.valueOf)
  ))
  val smallintArray: DuckDbType[Array[Short]] = DuckDbType(JavaDuckDbTypes.smallintArray.bimap(
    arr => arr.map(_.shortValue()),
    arr => arr.map(java.lang.Short.valueOf)
  ))
  val integerArray: DuckDbType[Array[Int]] = DuckDbType(JavaDuckDbTypes.integerArray.bimap(
    arr => arr.map(_.intValue()),
    arr => arr.map(java.lang.Integer.valueOf)
  ))
  val bigintArray: DuckDbType[Array[Long]] = DuckDbType(JavaDuckDbTypes.bigintArray.bimap(
    arr => arr.map(_.longValue()),
    arr => arr.map(java.lang.Long.valueOf)
  ))
  val floatArray: DuckDbType[Array[Float]] = DuckDbType(JavaDuckDbTypes.floatArray.bimap(
    arr => arr.map(_.floatValue()),
    arr => arr.map(java.lang.Float.valueOf)
  ))
  val doubleArray: DuckDbType[Array[Double]] = DuckDbType(JavaDuckDbTypes.doubleArray.bimap(
    arr => arr.map(_.doubleValue()),
    arr => arr.map(java.lang.Double.valueOf)
  ))
  val booleanArray: DuckDbType[Array[Boolean]] = DuckDbType(JavaDuckDbTypes.booleanArray.bimap(
    arr => arr.map(_.booleanValue()),
    arr => arr.map(java.lang.Boolean.valueOf)
  ))
  val decimalArray: DuckDbType[Array[BigDecimal]] = DuckDbType(JavaDuckDbTypes.decimalArray.bimap(
    arr => arr.map(BigDecimal(_)),
    arr => arr.map(_.bigDecimal)
  ))

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
  val blobArray = DuckDbType(JavaDuckDbTypes.blobArray)
  val dateArray = DuckDbType(JavaDuckDbTypes.dateArray)
  val timeArray = DuckDbType(JavaDuckDbTypes.timeArray)
  val timestampArray = DuckDbType(JavaDuckDbTypes.timestampArray)
  val timestamptzArray = DuckDbType(JavaDuckDbTypes.timestamptzArray)
  val intervalArray = DuckDbType(JavaDuckDbTypes.intervalArray)
  val uuidArray = DuckDbType(JavaDuckDbTypes.uuidArray)
  val jsonArray = DuckDbType(JavaDuckDbTypes.jsonArray)
  val listBoolean = DuckDbType(JavaDuckDbTypes.listBoolean)
  val listTinyint = DuckDbType(JavaDuckDbTypes.listTinyint)
  val listSmallint = DuckDbType(JavaDuckDbTypes.listSmallint)
  val listInteger = DuckDbType(JavaDuckDbTypes.listInteger)
  val listBigint = DuckDbType(JavaDuckDbTypes.listBigint)
  val listFloat = DuckDbType(JavaDuckDbTypes.listFloat)
  val listDouble = DuckDbType(JavaDuckDbTypes.listDouble)
  val listVarchar = DuckDbType(JavaDuckDbTypes.listVarchar)
  val listUuid = DuckDbType(JavaDuckDbTypes.listUuid)
  val listDate = DuckDbType(JavaDuckDbTypes.listDate)
  val listTime = DuckDbType(JavaDuckDbTypes.listTime)
  val listTimestamp = DuckDbType(JavaDuckDbTypes.listTimestamp)
  val listTimestamptz = DuckDbType(JavaDuckDbTypes.listTimestamptz)
  val listDecimal = DuckDbType(JavaDuckDbTypes.listDecimal)
  val listHugeint = DuckDbType(JavaDuckDbTypes.listHugeint)
  val listInterval = DuckDbType(JavaDuckDbTypes.listInterval)
  val unknown = DuckDbType(JavaDuckDbTypes.unknown)

  // Forward static methods with Scala type conversion
  def decimal(precision: Int, scale: Int): DuckDbType[BigDecimal] =
    DuckDbType(JavaDuckDbTypes.decimal(precision, scale).bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  def varchar(length: Int): DuckDbType[String] =
    DuckDbType(JavaDuckDbTypes.varchar(length))

  def char_(length: Int): DuckDbType[String] =
    DuckDbType(JavaDuckDbTypes.char_(length))

  def bit(length: Int): DuckDbType[String] =
    DuckDbType(JavaDuckDbTypes.bit(length))

  def ofEnum[E <: Enum[E]](enumTypeName: String, fromString: java.util.function.Function[String, E]): DuckDbType[E] =
    DuckDbType(JavaDuckDbTypes.ofEnum(enumTypeName, fromString))
}

object DuckDbTypes extends DuckDbTypes
