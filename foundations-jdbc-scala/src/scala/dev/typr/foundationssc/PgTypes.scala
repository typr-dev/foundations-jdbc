package dev.typr.foundationssc

import dev.typr.foundations.{PgTypes => JavaPgTypes}
import scala.jdk.CollectionConverters.*

/** Scala-friendly PgType instances that use Scala types instead of Java boxed types. All types from dev.typr.foundations.PgTypes are available here, with
  * primitives and BigDecimal converted to Scala types.
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

  // Forward all other types directly from Java
  val aclitem = PgType(JavaPgTypes.aclitem)
  val anyarray = PgType(JavaPgTypes.anyarray)
  val bit = PgType(JavaPgTypes.bit)
  val varbit = PgType(JavaPgTypes.varbit)
  val inet = PgType(JavaPgTypes.inet)
  val cidr = PgType(JavaPgTypes.cidr)
  val macaddr = PgType(JavaPgTypes.macaddr)
  val macaddr8 = PgType(JavaPgTypes.macaddr8)
  val timestamptz = PgType(JavaPgTypes.timestamptz)
  val int2vector = PgType(JavaPgTypes.int2vector)
  val json = PgType(JavaPgTypes.json)
  val jsonb = PgType(JavaPgTypes.jsonb)
  val date = PgType(JavaPgTypes.date)
  val timestamp = PgType(JavaPgTypes.timestamp)
  val time = PgType(JavaPgTypes.time)
  val oid = PgType(JavaPgTypes.oid)
  val money = PgType(JavaPgTypes.money)
  val name = PgType(JavaPgTypes.name)
  val timetz = PgType(JavaPgTypes.timetz)
  val oidvector = PgType(JavaPgTypes.oidvector)
  val interval = PgType(JavaPgTypes.interval)
  val box = PgType(JavaPgTypes.box)
  val circle = PgType(JavaPgTypes.circle)
  val line = PgType(JavaPgTypes.line)
  val lseg = PgType(JavaPgTypes.lseg)
  val path = PgType(JavaPgTypes.path)
  val point = PgType(JavaPgTypes.point)
  val polygon = PgType(JavaPgTypes.polygon)
  val pgNodeTree = PgType(JavaPgTypes.pgNodeTree)
  val regclass = PgType(JavaPgTypes.regclass)
  val regconfig = PgType(JavaPgTypes.regconfig)
  val regdictionary = PgType(JavaPgTypes.regdictionary)
  val regnamespace = PgType(JavaPgTypes.regnamespace)
  val regoper = PgType(JavaPgTypes.regoper)
  val regoperator = PgType(JavaPgTypes.regoperator)
  val regproc = PgType(JavaPgTypes.regproc)
  val regprocedure = PgType(JavaPgTypes.regprocedure)
  val regrole = PgType(JavaPgTypes.regrole)
  val regtype = PgType(JavaPgTypes.regtype)
  val smallserial: PgType[Short] = PgType(JavaPgTypes.smallserial.transform(s => s, s => s))
  val bpchar = PgType(JavaPgTypes.bpchar)
  val text = PgType(JavaPgTypes.text)
  val uuid = PgType(JavaPgTypes.uuid)
  val xid = PgType(JavaPgTypes.xid)
  val xml = PgType(JavaPgTypes.xml)
  val vector = PgType(JavaPgTypes.vector)
  val unknown = PgType(JavaPgTypes.unknown)
  val bytea = PgType(JavaPgTypes.bytea)
  val int4range = PgType(JavaPgTypes.int4range)
  val int8range = PgType(JavaPgTypes.int8range)
  val numrange = PgType(JavaPgTypes.numrange)
  val daterange = PgType(JavaPgTypes.daterange)
  val tsrange = PgType(JavaPgTypes.tsrange)
  val tstzrange = PgType(JavaPgTypes.tstzrange)
  val record = PgType(JavaPgTypes.record)

  // Forward static methods
  def ofEnum[E <: Enum[E]](sqlType: String, fromString: java.util.function.Function[String, E]): PgType[E] =
    PgType(JavaPgTypes.ofEnum(sqlType, fromString))

  def ofPgObject[T](
      sqlType: String,
      constructor: dev.typr.foundations.SqlFunction[String, T],
      extractor: java.util.function.Function[T, String],
      json: dev.typr.foundations.PgJson[T]
  ): PgType[T] =
    PgType(JavaPgTypes.ofPgObject(sqlType, constructor, extractor, json))

  def record(sqlType: String): PgType[dev.typr.foundations.data.Record] =
    PgType(JavaPgTypes.record(sqlType))

  def bit(n: Int): PgType[dev.typr.foundations.data.Bit] =
    PgType(JavaPgTypes.bit(n))

  def bpchar(precision: Int): PgType[String] =
    PgType(JavaPgTypes.bpchar(precision))

  def pgObject[T <: org.postgresql.util.PGobject](
      sqlType: String,
      clazz: Class[T],
      json: dev.typr.foundations.PgJson[T]
  ): PgType[T] =
    PgType(JavaPgTypes.pgObject(sqlType, clazz, json))

  def rangeType[T <: Comparable[? >: T]](
      sqlType: String,
      valueParser: dev.typr.foundations.SqlFunction[String, T],
      rangeFactory: java.util.function.BiFunction[dev.typr.foundations.data.RangeBound[T], dev.typr.foundations.data.RangeBound[
        T
      ], dev.typr.foundations.data.Range[T]],
      json: dev.typr.foundations.PgJson[dev.typr.foundations.data.Range[T]]
  ): PgType[dev.typr.foundations.data.Range[T]] =
    PgType(JavaPgTypes.rangeType(sqlType, valueParser, rangeFactory, json))

  /** Build an ad-hoc composite PgType from a RowCodec. Read-only, for row constructors. */
  def compositeOf[Row](codec: RowCodecNamed[Row]): PgType[Row] =
    PgType(JavaPgTypes.compositeOf(codec.underlying))

  /** Build a named composite PgType from a RowCodec. Read-write, for CREATE TYPE declarations. */
  def compositeOf[Row](sqlType: String, codec: RowCodecNamed[Row]): PgType[Row] =
    PgType(JavaPgTypes.compositeOf(sqlType, codec.underlying))

  // JSON-encoded row types (json)

  def jsonArrayEncoded[Row](parser: RowCodec[Row]): PgType[Row] =
    PgType(JavaPgTypes.jsonArrayEncoded(parser.underlying))

  def jsonArrayEncodedList[Row](parser: RowCodec[Row]): PgType[List[Row]] =
    PgType(
      JavaPgTypes
        .jsonArrayEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )

  def jsonObjectEncoded[Row](parser: RowCodecNamed[Row]): PgType[Row] =
    PgType(JavaPgTypes.jsonObjectEncoded(parser.underlying))

  def jsonObjectEncodedList[Row](parser: RowCodecNamed[Row]): PgType[List[Row]] =
    PgType(
      JavaPgTypes
        .jsonObjectEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )

  // JSON-encoded row types (jsonb)

  def jsonbArrayEncoded[Row](parser: RowCodec[Row]): PgType[Row] =
    PgType(JavaPgTypes.jsonbArrayEncoded(parser.underlying))

  def jsonbArrayEncodedList[Row](parser: RowCodec[Row]): PgType[List[Row]] =
    PgType(
      JavaPgTypes
        .jsonbArrayEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )

  def jsonbObjectEncoded[Row](parser: RowCodecNamed[Row]): PgType[Row] =
    PgType(JavaPgTypes.jsonbObjectEncoded(parser.underlying))

  def jsonbObjectEncodedList[Row](parser: RowCodecNamed[Row]): PgType[List[Row]] =
    PgType(
      JavaPgTypes
        .jsonbObjectEncodedList(parser.underlying)
        .transform(
          jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
          slist => java.util.List.copyOf(scala.jdk.CollectionConverters.SeqHasAsJava(slist).asJava)
        )
    )
}

object PgTypes extends PgTypes
