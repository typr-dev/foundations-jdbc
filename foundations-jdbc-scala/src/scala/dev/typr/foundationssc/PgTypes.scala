package dev.typr.foundationssc

import dev.typr.foundations.{PgTypes => JavaPgTypes}
import scala.jdk.CollectionConverters.*

/** Scala-friendly PgType instances that use Scala types instead of Java boxed types.
  * All types from dev.typr.foundations.PgTypes are available here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class PgTypes {
  // Primitives - convert Java boxed types to Scala native types
  val bool: PgType[Boolean] = PgType(JavaPgTypes.bool.transform(b => b, b => b))
  val int2: PgType[Short] = PgType(JavaPgTypes.int2.transform(s => s, s => s))
  val smallint: PgType[Short] = PgType(JavaPgTypes.smallint.transform(s => s, s => s))
  val int4: PgType[Int] = PgType(JavaPgTypes.int4.transform(i => i, i => i))
  val int8: PgType[Long] = PgType(JavaPgTypes.int8.transform(l => l, l => l))
  val float4: PgType[Float] = PgType(JavaPgTypes.float4.transform(f => f, f => f))
  val float8: PgType[Double] = PgType(JavaPgTypes.float8.transform(d => d, d => d))

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val numeric: PgType[BigDecimal] = PgType(JavaPgTypes.numeric.transform(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal))

  // Collections - convert Java collections to Scala collections
  val hstore: PgType[Map[String, String]] = PgType(JavaPgTypes.hstore.transform(javaMap => javaMap.asScala.toMap, scalaMap => scalaMap.asJava))

  // Array types - convert Java boxed arrays to Scala native arrays
  val boolArray: PgType[Array[Boolean]] = PgType(JavaPgTypes.boolArray.transform(
    arr => arr.map(_.booleanValue()),
    arr => arr.map(java.lang.Boolean.valueOf)
  ))
  val int2Array: PgType[Array[Short]] = PgType(JavaPgTypes.int2Array.transform(
    arr => arr.map(_.shortValue()),
    arr => arr.map(java.lang.Short.valueOf)
  ))
  val smallintArray: PgType[Array[Short]] = int2Array
  val int4Array: PgType[Array[Int]] = PgType(JavaPgTypes.int4Array.transform(
    arr => arr.map(_.intValue()),
    arr => arr.map(java.lang.Integer.valueOf)
  ))
  val int8Array: PgType[Array[Long]] = PgType(JavaPgTypes.int8Array.transform(
    arr => arr.map(_.longValue()),
    arr => arr.map(java.lang.Long.valueOf)
  ))
  val float4Array: PgType[Array[Float]] = PgType(JavaPgTypes.float4Array.transform(
    arr => arr.map(_.floatValue()),
    arr => arr.map(java.lang.Float.valueOf)
  ))
  val float8Array: PgType[Array[Double]] = PgType(JavaPgTypes.float8Array.transform(
    arr => arr.map(_.doubleValue()),
    arr => arr.map(java.lang.Double.valueOf)
  ))
  val numericArray: PgType[Array[BigDecimal]] = PgType(JavaPgTypes.numericArray.transform(
    arr => arr.map(BigDecimal(_)),
    arr => arr.map(_.bigDecimal)
  ))

  // Forward all other types directly from Java
  val aclitem = PgType(JavaPgTypes.aclitem)
  val aclitemArray = PgType(JavaPgTypes.aclitemArray)
  val anyarray = PgType(JavaPgTypes.anyarray)
  val anyarrayArray = PgType(JavaPgTypes.anyarrayArray)
  val boolArrayUnboxed = PgType(JavaPgTypes.boolArrayUnboxed)
  val bit = PgType(JavaPgTypes.bit)
  val bitArray = PgType(JavaPgTypes.bitArray)
  val varbit = PgType(JavaPgTypes.varbit)
  val varbitArray = PgType(JavaPgTypes.varbitArray)
  val float8ArrayUnboxed = PgType(JavaPgTypes.float8ArrayUnboxed)
  val float4ArrayUnboxed = PgType(JavaPgTypes.float4ArrayUnboxed)
  val inet = PgType(JavaPgTypes.inet)
  val inetArray = PgType(JavaPgTypes.inetArray)
  val cidr = PgType(JavaPgTypes.cidr)
  val cidrArray = PgType(JavaPgTypes.cidrArray)
  val macaddr = PgType(JavaPgTypes.macaddr)
  val macaddrArray = PgType(JavaPgTypes.macaddrArray)
  val macaddr8 = PgType(JavaPgTypes.macaddr8)
  val macaddr8Array = PgType(JavaPgTypes.macaddr8Array)
  val timestamptz = PgType(JavaPgTypes.timestamptz)
  val timestamptzArray = PgType(JavaPgTypes.timestamptzArray)
  val int2vector = PgType(JavaPgTypes.int2vector)
  val int2vectorArray = PgType(JavaPgTypes.int2vectorArray)
  val int4ArrayUnboxed = PgType(JavaPgTypes.int4ArrayUnboxed)
  val json = PgType(JavaPgTypes.json)
  val jsonArray = PgType(JavaPgTypes.jsonArray)
  val jsonb = PgType(JavaPgTypes.jsonb)
  val jsonbArray = PgType(JavaPgTypes.jsonbArray)
  val date = PgType(JavaPgTypes.date)
  val timestamp = PgType(JavaPgTypes.timestamp)
  val timestampArray = PgType(JavaPgTypes.timestampArray)
  val dateArray = PgType(JavaPgTypes.dateArray)
  val time = PgType(JavaPgTypes.time)
  val timeArray = PgType(JavaPgTypes.timeArray)
  val int8ArrayUnboxed = PgType(JavaPgTypes.int8ArrayUnboxed)
  val oid = PgType(JavaPgTypes.oid)
  val oidArray = PgType(JavaPgTypes.oidArray)
  val money = PgType(JavaPgTypes.money)
  val moneyArray = PgType(JavaPgTypes.moneyArray)
  val name = PgType(JavaPgTypes.name)
  val nameArray = PgType(JavaPgTypes.nameArray)
  val timetz = PgType(JavaPgTypes.timetz)
  val timetzArray = PgType(JavaPgTypes.timetzArray)
  val oidvector = PgType(JavaPgTypes.oidvector)
  val oidvectorArray = PgType(JavaPgTypes.oidvectorArray)
  val interval = PgType(JavaPgTypes.interval)
  val intervalArray = PgType(JavaPgTypes.intervalArray)
  val box = PgType(JavaPgTypes.box)
  val boxArray = PgType(JavaPgTypes.boxArray)
  val circle = PgType(JavaPgTypes.circle)
  val circleArray = PgType(JavaPgTypes.circleArray)
  val line = PgType(JavaPgTypes.line)
  val lineArray = PgType(JavaPgTypes.lineArray)
  val lseg = PgType(JavaPgTypes.lseg)
  val lsegArray = PgType(JavaPgTypes.lsegArray)
  val path = PgType(JavaPgTypes.path)
  val pathArray = PgType(JavaPgTypes.pathArray)
  val point = PgType(JavaPgTypes.point)
  val pointArray = PgType(JavaPgTypes.pointArray)
  val polygon = PgType(JavaPgTypes.polygon)
  val polygonArray = PgType(JavaPgTypes.polygonArray)
  val pgNodeTree = PgType(JavaPgTypes.pgNodeTree)
  val pgNodeTreeArray = PgType(JavaPgTypes.pgNodeTreeArray)
  val regclass = PgType(JavaPgTypes.regclass)
  val regclassArray = PgType(JavaPgTypes.regclassArray)
  val regconfig = PgType(JavaPgTypes.regconfig)
  val regconfigArray = PgType(JavaPgTypes.regconfigArray)
  val regdictionary = PgType(JavaPgTypes.regdictionary)
  val regdictionaryArray = PgType(JavaPgTypes.regdictionaryArray)
  val regnamespace = PgType(JavaPgTypes.regnamespace)
  val regnamespaceArray = PgType(JavaPgTypes.regnamespaceArray)
  val regoper = PgType(JavaPgTypes.regoper)
  val regoperArray = PgType(JavaPgTypes.regoperArray)
  val regoperator = PgType(JavaPgTypes.regoperator)
  val regoperatorArray = PgType(JavaPgTypes.regoperatorArray)
  val regproc = PgType(JavaPgTypes.regproc)
  val regprocArray = PgType(JavaPgTypes.regprocArray)
  val regprocedure = PgType(JavaPgTypes.regprocedure)
  val regprocedureArray = PgType(JavaPgTypes.regprocedureArray)
  val regrole = PgType(JavaPgTypes.regrole)
  val regroleArray = PgType(JavaPgTypes.regroleArray)
  val regtype = PgType(JavaPgTypes.regtype)
  val regtypeArray = PgType(JavaPgTypes.regtypeArray)
  val int2ArrayUnboxed = PgType(JavaPgTypes.int2ArrayUnboxed)
  val smallintArrayUnboxed = PgType(JavaPgTypes.smallintArrayUnboxed)
  val bpchar = PgType(JavaPgTypes.bpchar)
  val text = PgType(JavaPgTypes.text)
  val bpcharArray = PgType(JavaPgTypes.bpcharArray)
  val textArray = PgType(JavaPgTypes.textArray)
  val uuid = PgType(JavaPgTypes.uuid)
  val uuidArray = PgType(JavaPgTypes.uuidArray)
  val xid = PgType(JavaPgTypes.xid)
  val xidArray = PgType(JavaPgTypes.xidArray)
  val xml = PgType(JavaPgTypes.xml)
  val xmlArray = PgType(JavaPgTypes.xmlArray)
  val vector = PgType(JavaPgTypes.vector)
  val vectorArray = PgType(JavaPgTypes.vectorArray)
  val unknown = PgType(JavaPgTypes.unknown)
  val unknownArray = PgType(JavaPgTypes.unknownArray)
  val bytea = PgType(JavaPgTypes.bytea)
  val int4range = PgType(JavaPgTypes.int4range)
  val int4rangeArray = PgType(JavaPgTypes.int4rangeArray)
  val int8range = PgType(JavaPgTypes.int8range)
  val int8rangeArray = PgType(JavaPgTypes.int8rangeArray)
  val numrange = PgType(JavaPgTypes.numrange)
  val numrangeArray = PgType(JavaPgTypes.numrangeArray)
  val daterange = PgType(JavaPgTypes.daterange)
  val daterangeArray = PgType(JavaPgTypes.daterangeArray)
  val tsrange = PgType(JavaPgTypes.tsrange)
  val tsrangeArray = PgType(JavaPgTypes.tsrangeArray)
  val tstzrange = PgType(JavaPgTypes.tstzrange)
  val tstzrangeArray = PgType(JavaPgTypes.tstzrangeArray)
  val record = PgType(JavaPgTypes.record)
  val recordArray = PgType(JavaPgTypes.recordArray)

  // Forward static methods
  def ofEnum[E <: Enum[E]](sqlType: String, fromString: java.util.function.Function[String, E]): PgType[E] =
    PgType(JavaPgTypes.ofEnum(sqlType, fromString))

  def ofPgObject[T](sqlType: String, constructor: dev.typr.foundations.SqlFunction[String, T], extractor: java.util.function.Function[T, String], json: dev.typr.foundations.PgJson[T]): PgType[T] =
    PgType(JavaPgTypes.ofPgObject(sqlType, constructor, extractor, json))

  def record(sqlType: String): PgType[dev.typr.foundations.data.Record] =
    PgType(JavaPgTypes.record(sqlType))

  def recordArray(sqlType: String): PgType[Array[dev.typr.foundations.data.Record]] =
    PgType(JavaPgTypes.recordArray(sqlType))

  def bpchar(precision: Int): PgType[String] =
    PgType(JavaPgTypes.bpchar(precision))

  def bpcharArray(n: Int): PgType[Array[String]] =
    PgType(JavaPgTypes.bpcharArray(n))

  // JSON-encoded row types (json)

  def jsonArrayEncoded[Row](parser: RowCodec[Row]): PgType[Row] =
    PgType(JavaPgTypes.jsonArrayEncoded(parser.underlying))

  def jsonArrayEncodedList[Row](parser: RowCodec[Row]): PgType[List[Row]] =
    PgType(JavaPgTypes.jsonArrayEncodedList(parser.underlying).transform(
      jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
      slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
    ))

  def jsonObjectEncoded[Row](parser: RowCodecNamed[Row]): PgType[Row] =
    PgType(JavaPgTypes.jsonObjectEncoded(parser.underlying))

  def jsonObjectEncodedList[Row](parser: RowCodecNamed[Row]): PgType[List[Row]] =
    PgType(JavaPgTypes.jsonObjectEncodedList(parser.underlying).transform(
      jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
      slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
    ))

  // JSON-encoded row types (jsonb)

  def jsonbArrayEncoded[Row](parser: RowCodec[Row]): PgType[Row] =
    PgType(JavaPgTypes.jsonbArrayEncoded(parser.underlying))

  def jsonbArrayEncodedList[Row](parser: RowCodec[Row]): PgType[List[Row]] =
    PgType(JavaPgTypes.jsonbArrayEncodedList(parser.underlying).transform(
      jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
      slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
    ))

  def jsonbObjectEncoded[Row](parser: RowCodecNamed[Row]): PgType[Row] =
    PgType(JavaPgTypes.jsonbObjectEncoded(parser.underlying))

  def jsonbObjectEncodedList[Row](parser: RowCodecNamed[Row]): PgType[List[Row]] =
    PgType(JavaPgTypes.jsonbObjectEncodedList(parser.underlying).transform(
      jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
      slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
    ))
}

object PgTypes extends PgTypes
