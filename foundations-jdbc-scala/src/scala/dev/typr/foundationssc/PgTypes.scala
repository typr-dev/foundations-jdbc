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
  val aclitem: PgType[dev.typr.foundations.data.AclItem] = PgType(JavaPgTypes.aclitem)
  val anyarray: PgType[dev.typr.foundations.data.AnyArray] = PgType(JavaPgTypes.anyarray)
  val bit: PgType[dev.typr.foundations.data.Bit] = PgType(JavaPgTypes.bit)
  val varbit: PgType[dev.typr.foundations.data.Varbit] = PgType(JavaPgTypes.varbit)
  val inet: PgType[dev.typr.foundations.data.Inet] = PgType(JavaPgTypes.inet)
  val cidr: PgType[dev.typr.foundations.data.Cidr] = PgType(JavaPgTypes.cidr)
  val macaddr: PgType[dev.typr.foundations.data.MacAddr] = PgType(JavaPgTypes.macaddr)
  val macaddr8: PgType[dev.typr.foundations.data.MacAddr8] = PgType(JavaPgTypes.macaddr8)
  val timestamptz: PgType[java.time.Instant] = PgType(JavaPgTypes.timestamptz)
  val int2vector: PgType[dev.typr.foundations.data.Int2Vector] = PgType(JavaPgTypes.int2vector)
  val json: PgType[dev.typr.foundations.data.Json] = PgType(JavaPgTypes.json)
  val jsonb: PgType[dev.typr.foundations.data.Jsonb] = PgType(JavaPgTypes.jsonb)
  val date: PgType[java.time.LocalDate] = PgType(JavaPgTypes.date)
  val timestamp: PgType[java.time.LocalDateTime] = PgType(JavaPgTypes.timestamp)
  val time: PgType[java.time.LocalTime] = PgType(JavaPgTypes.time)
  val oid: PgType[dev.typr.foundations.data.Oid] = PgType(JavaPgTypes.oid)
  val money: PgType[dev.typr.foundations.data.Money] = PgType(JavaPgTypes.money)
  val name: PgType[String] = PgType(JavaPgTypes.name)
  val timetz: PgType[java.time.OffsetTime] = PgType(JavaPgTypes.timetz)
  val oidvector: PgType[dev.typr.foundations.data.OidVector] = PgType(JavaPgTypes.oidvector)
  val interval: PgType[org.postgresql.util.PGInterval] = PgType(JavaPgTypes.interval)
  val box: PgType[org.postgresql.geometric.PGbox] = PgType(JavaPgTypes.box)
  val circle: PgType[org.postgresql.geometric.PGcircle] = PgType(JavaPgTypes.circle)
  val line: PgType[org.postgresql.geometric.PGline] = PgType(JavaPgTypes.line)
  val lseg: PgType[org.postgresql.geometric.PGlseg] = PgType(JavaPgTypes.lseg)
  val path: PgType[org.postgresql.geometric.PGpath] = PgType(JavaPgTypes.path)
  val point: PgType[org.postgresql.geometric.PGpoint] = PgType(JavaPgTypes.point)
  val polygon: PgType[org.postgresql.geometric.PGpolygon] = PgType(JavaPgTypes.polygon)
  val pgNodeTree: PgType[dev.typr.foundations.data.PgNodeTree] = PgType(JavaPgTypes.pgNodeTree)
  val regclass: PgType[dev.typr.foundations.data.Regclass] = PgType(JavaPgTypes.regclass)
  val regconfig: PgType[dev.typr.foundations.data.Regconfig] = PgType(JavaPgTypes.regconfig)
  val regdictionary: PgType[dev.typr.foundations.data.Regdictionary] = PgType(JavaPgTypes.regdictionary)
  val regnamespace: PgType[dev.typr.foundations.data.Regnamespace] = PgType(JavaPgTypes.regnamespace)
  val regoper: PgType[dev.typr.foundations.data.Regoper] = PgType(JavaPgTypes.regoper)
  val regoperator: PgType[dev.typr.foundations.data.Regoperator] = PgType(JavaPgTypes.regoperator)
  val regproc: PgType[dev.typr.foundations.data.Regproc] = PgType(JavaPgTypes.regproc)
  val regprocedure: PgType[dev.typr.foundations.data.Regprocedure] = PgType(JavaPgTypes.regprocedure)
  val regrole: PgType[dev.typr.foundations.data.Regrole] = PgType(JavaPgTypes.regrole)
  val regtype: PgType[dev.typr.foundations.data.Regtype] = PgType(JavaPgTypes.regtype)
  val smallserial: PgType[Short] = PgType(JavaPgTypes.smallserial.transform(s => s, s => s))
  val bpchar: PgType[String] = PgType(JavaPgTypes.bpchar)
  val text: PgType[String] = PgType(JavaPgTypes.text)
  val uuid: PgType[java.util.UUID] = PgType(JavaPgTypes.uuid)
  val xid: PgType[dev.typr.foundations.data.Xid] = PgType(JavaPgTypes.xid)
  val xml: PgType[dev.typr.foundations.data.Xml] = PgType(JavaPgTypes.xml)
  val vector: PgType[dev.typr.foundations.data.Vector] = PgType(JavaPgTypes.vector)
  val unknown: PgType[dev.typr.foundations.data.Unknown] = PgType(JavaPgTypes.unknown)
  val bytea: PgType[Array[Byte]] = PgType(JavaPgTypes.bytea)
  val int4range: PgType[dev.typr.foundations.data.Range[Integer]] = PgType(JavaPgTypes.int4range)
  val int8range: PgType[dev.typr.foundations.data.Range[java.lang.Long]] = PgType(JavaPgTypes.int8range)
  val numrange: PgType[dev.typr.foundations.data.Range[java.math.BigDecimal]] = PgType(JavaPgTypes.numrange)
  val daterange: PgType[dev.typr.foundations.data.Range[java.time.LocalDate]] = PgType(JavaPgTypes.daterange)
  val tsrange: PgType[dev.typr.foundations.data.Range[java.time.LocalDateTime]] = PgType(JavaPgTypes.tsrange)
  val tstzrange: PgType[dev.typr.foundations.data.Range[java.time.Instant]] = PgType(JavaPgTypes.tstzrange)
  val record: PgType[dev.typr.foundations.data.Record] = PgType(JavaPgTypes.record)

  // Forward static methods
  def ofEnum[E <: Enum[E]](sqlType: String, fromString: java.util.function.Function[String, E]): PgType[E] =
    PgType(JavaPgTypes.ofEnum(sqlType, fromString))

  def ofEnum[E <: AnyRef](sqlType: String, values: Array[E]): PgType[E] =
    ofEnum(sqlType, values, ((e: E) => e.toString): java.util.function.Function[E, String])

  def ofEnum[E <: AnyRef](sqlType: String, values: Array[E], name: java.util.function.Function[E, String]): PgType[E] =
    PgType(JavaPgTypes.ofEnum(sqlType, values.asInstanceOf[Array[Object & E]], name))

  def ofPgObject[T](
      sqlType: String,
      constructor: dev.typr.foundations.SqlFunction[String, T],
      extractor: java.util.function.Function[T, String],
      json: dev.typr.foundations.PgJson[T]
  ): PgType[T] =
    PgType(JavaPgTypes.ofPgObject(sqlType, constructor, extractor, json))

  def recordOf(sqlType: String): PgType[dev.typr.foundations.data.Record] =
    PgType(JavaPgTypes.recordOf(sqlType))

  def bitOf(n: Int): PgType[dev.typr.foundations.data.Bit] =
    PgType(JavaPgTypes.bitOf(n))

  def bpcharOf(precision: Int): PgType[String] =
    PgType(JavaPgTypes.bpcharOf(precision))

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
