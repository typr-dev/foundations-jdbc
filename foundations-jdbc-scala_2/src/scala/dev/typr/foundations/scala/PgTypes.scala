package dev.typr.foundations.scala

import dev.typr.foundations.{PgType, PgTypes => JavaPgTypes}
import scala.jdk.CollectionConverters._

/** Scala-friendly PgType instances that use Scala types instead of Java boxed types.
  * All types from dev.typr.foundations.PgTypes are available here, with primitives and BigDecimal converted to Scala types.
  *
  * Extend this class to add your own custom types to a shared set of type definitions.
  */
class PgTypes {
  // Primitives - convert Java boxed types to Scala native types
  val bool: PgType[Boolean] = JavaPgTypes.bool.bimap(b => b, b => b)
  val int2: PgType[Short] = JavaPgTypes.int2.bimap(s => s, s => s)
  val smallint: PgType[Short] = JavaPgTypes.smallint.bimap(s => s, s => s)
  val int4: PgType[Int] = JavaPgTypes.int4.bimap(i => i, i => i)
  val int8: PgType[Long] = JavaPgTypes.int8.bimap(l => l, l => l)
  val float4: PgType[Float] = JavaPgTypes.float4.bimap(f => f, f => f)
  val float8: PgType[Double] = JavaPgTypes.float8.bimap(d => d, d => d)

  // BigDecimal - convert Java BigDecimal to Scala BigDecimal
  val numeric: PgType[BigDecimal] = JavaPgTypes.numeric.bimap(jbd => BigDecimal(jbd), sbd => sbd.bigDecimal)

  // Collections - convert Java collections to Scala collections
  val hstore: PgType[Map[String, String]] = JavaPgTypes.hstore.bimap(javaMap => javaMap.asScala.toMap, scalaMap => scalaMap.asJava)

  // Array types - convert Java boxed arrays to Scala native arrays
  val boolArray: PgType[Array[Boolean]] = JavaPgTypes.boolArray.bimap(
    arr => arr.map(_.booleanValue()),
    arr => arr.map(java.lang.Boolean.valueOf)
  )
  val int2Array: PgType[Array[Short]] = JavaPgTypes.int2Array.bimap(
    arr => arr.map(_.shortValue()),
    arr => arr.map(java.lang.Short.valueOf)
  )
  val smallintArray: PgType[Array[Short]] = int2Array
  val int4Array: PgType[Array[Int]] = JavaPgTypes.int4Array.bimap(
    arr => arr.map(_.intValue()),
    arr => arr.map(java.lang.Integer.valueOf)
  )
  val int8Array: PgType[Array[Long]] = JavaPgTypes.int8Array.bimap(
    arr => arr.map(_.longValue()),
    arr => arr.map(java.lang.Long.valueOf)
  )
  val float4Array: PgType[Array[Float]] = JavaPgTypes.float4Array.bimap(
    arr => arr.map(_.floatValue()),
    arr => arr.map(java.lang.Float.valueOf)
  )
  val float8Array: PgType[Array[Double]] = JavaPgTypes.float8Array.bimap(
    arr => arr.map(_.doubleValue()),
    arr => arr.map(java.lang.Double.valueOf)
  )
  val numericArray: PgType[Array[BigDecimal]] = JavaPgTypes.numericArray.bimap(
    arr => arr.map(BigDecimal(_)),
    arr => arr.map(_.bigDecimal)
  )

  // Forward all other types directly from Java
  val aclitem = JavaPgTypes.aclitem
  val aclitemArray = JavaPgTypes.aclitemArray
  val anyarray = JavaPgTypes.anyarray
  val anyarrayArray = JavaPgTypes.anyarrayArray
  val boolArrayUnboxed = JavaPgTypes.boolArrayUnboxed
  val float8ArrayUnboxed = JavaPgTypes.float8ArrayUnboxed
  val float4ArrayUnboxed = JavaPgTypes.float4ArrayUnboxed
  val inet = JavaPgTypes.inet
  val inetArray = JavaPgTypes.inetArray
  val cidr = JavaPgTypes.cidr
  val cidrArray = JavaPgTypes.cidrArray
  val macaddr = JavaPgTypes.macaddr
  val macaddrArray = JavaPgTypes.macaddrArray
  val macaddr8 = JavaPgTypes.macaddr8
  val macaddr8Array = JavaPgTypes.macaddr8Array
  val timestamptz = JavaPgTypes.timestamptz
  val timestamptzArray = JavaPgTypes.timestamptzArray
  val int2vector = JavaPgTypes.int2vector
  val int2vectorArray = JavaPgTypes.int2vectorArray
  val int4ArrayUnboxed = JavaPgTypes.int4ArrayUnboxed
  val json = JavaPgTypes.json
  val jsonArray = JavaPgTypes.jsonArray
  val jsonb = JavaPgTypes.jsonb
  val jsonbArray = JavaPgTypes.jsonbArray
  val date = JavaPgTypes.date
  val timestamp = JavaPgTypes.timestamp
  val timestampArray = JavaPgTypes.timestampArray
  val dateArray = JavaPgTypes.dateArray
  val time = JavaPgTypes.time
  val timeArray = JavaPgTypes.timeArray
  val int8ArrayUnboxed = JavaPgTypes.int8ArrayUnboxed
  val oid = JavaPgTypes.oid
  val oidArray = JavaPgTypes.oidArray
  val money = JavaPgTypes.money
  val moneyArray = JavaPgTypes.moneyArray
  val name = JavaPgTypes.name
  val nameArray = JavaPgTypes.nameArray
  val timetz = JavaPgTypes.timetz
  val timetzArray = JavaPgTypes.timetzArray
  val oidvector = JavaPgTypes.oidvector
  val oidvectorArray = JavaPgTypes.oidvectorArray
  val interval = JavaPgTypes.interval
  val intervalArray = JavaPgTypes.intervalArray
  val box = JavaPgTypes.box
  val boxArray = JavaPgTypes.boxArray
  val circle = JavaPgTypes.circle
  val circleArray = JavaPgTypes.circleArray
  val line = JavaPgTypes.line
  val lineArray = JavaPgTypes.lineArray
  val lseg = JavaPgTypes.lseg
  val lsegArray = JavaPgTypes.lsegArray
  val path = JavaPgTypes.path
  val pathArray = JavaPgTypes.pathArray
  val point = JavaPgTypes.point
  val pointArray = JavaPgTypes.pointArray
  val polygon = JavaPgTypes.polygon
  val polygonArray = JavaPgTypes.polygonArray
  val pgNodeTree = JavaPgTypes.pgNodeTree
  val pgNodeTreeArray = JavaPgTypes.pgNodeTreeArray
  val regclass = JavaPgTypes.regclass
  val regclassArray = JavaPgTypes.regclassArray
  val regconfig = JavaPgTypes.regconfig
  val regconfigArray = JavaPgTypes.regconfigArray
  val regdictionary = JavaPgTypes.regdictionary
  val regdictionaryArray = JavaPgTypes.regdictionaryArray
  val regnamespace = JavaPgTypes.regnamespace
  val regnamespaceArray = JavaPgTypes.regnamespaceArray
  val regoper = JavaPgTypes.regoper
  val regoperArray = JavaPgTypes.regoperArray
  val regoperator = JavaPgTypes.regoperator
  val regoperatorArray = JavaPgTypes.regoperatorArray
  val regproc = JavaPgTypes.regproc
  val regprocArray = JavaPgTypes.regprocArray
  val regprocedure = JavaPgTypes.regprocedure
  val regprocedureArray = JavaPgTypes.regprocedureArray
  val regrole = JavaPgTypes.regrole
  val regroleArray = JavaPgTypes.regroleArray
  val regtype = JavaPgTypes.regtype
  val regtypeArray = JavaPgTypes.regtypeArray
  val int2ArrayUnboxed = JavaPgTypes.int2ArrayUnboxed
  val smallintArrayUnboxed = JavaPgTypes.smallintArrayUnboxed
  val bpchar = JavaPgTypes.bpchar
  val text = JavaPgTypes.text
  val bpcharArray = JavaPgTypes.bpcharArray
  val textArray = JavaPgTypes.textArray
  val uuid = JavaPgTypes.uuid
  val uuidArray = JavaPgTypes.uuidArray
  val xid = JavaPgTypes.xid
  val xidArray = JavaPgTypes.xidArray
  val xml = JavaPgTypes.xml
  val xmlArray = JavaPgTypes.xmlArray
  val vector = JavaPgTypes.vector
  val vectorArray = JavaPgTypes.vectorArray
  val unknown = JavaPgTypes.unknown
  val unknownArray = JavaPgTypes.unknownArray
  val bytea = JavaPgTypes.bytea
  val int4range = JavaPgTypes.int4range
  val int4rangeArray = JavaPgTypes.int4rangeArray
  val int8range = JavaPgTypes.int8range
  val int8rangeArray = JavaPgTypes.int8rangeArray
  val numrange = JavaPgTypes.numrange
  val numrangeArray = JavaPgTypes.numrangeArray
  val daterange = JavaPgTypes.daterange
  val daterangeArray = JavaPgTypes.daterangeArray
  val tsrange = JavaPgTypes.tsrange
  val tsrangeArray = JavaPgTypes.tsrangeArray
  val tstzrange = JavaPgTypes.tstzrange
  val tstzrangeArray = JavaPgTypes.tstzrangeArray
  val record = JavaPgTypes.record
  val recordArray = JavaPgTypes.recordArray

  // Forward static methods
  def ofEnum[E <: Enum[E]](sqlType: String, fromString: java.util.function.Function[String, E]): PgType[E] =
    JavaPgTypes.ofEnum(sqlType, fromString)

  def ofPgObject[T](sqlType: String, constructor: dev.typr.foundations.SqlFunction[String, T], extractor: java.util.function.Function[T, String], json: dev.typr.foundations.PgJson[T]): PgType[T] =
    JavaPgTypes.ofPgObject(sqlType, constructor, extractor, json)

  def record(sqlType: String): PgType[dev.typr.foundations.data.Record] =
    JavaPgTypes.record(sqlType)

  def recordArray(sqlType: String): PgType[Array[dev.typr.foundations.data.Record]] =
    JavaPgTypes.recordArray(sqlType)

  def bpchar(precision: Int): PgType[String] =
    JavaPgTypes.bpchar(precision)

  def bpcharArray(n: Int): PgType[Array[String]] =
    JavaPgTypes.bpcharArray(n)
}

object PgTypes extends PgTypes
