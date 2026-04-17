package dev.typr.foundationskt

import dev.typr.foundations.SqlFunction
import dev.typr.foundations.PgTypes as JavaPgTypes

/**
 * Kotlin-friendly PgType instances that use Kotlin types instead of Java boxed types.
 * All types from dev.typr.foundations.PgTypes are available here, with primitives converted to Kotlin types.
 *
 * Extend this class to add your own custom types to a shared set of type definitions.
 */
open class PgTypes {
    // Primitives - convert Java boxed types to Kotlin native types
    open val bool: PgType<Boolean> = PgType(JavaPgTypes.bool.transform(SqlFunction { it }, { it }))
    open val int2: PgType<Short> = PgType(JavaPgTypes.int2.transform(SqlFunction { it }, { it }))
    open val smallint: PgType<Short> = PgType(JavaPgTypes.smallint.transform(SqlFunction { it }, { it }))
    open val int4: PgType<Int> = PgType(JavaPgTypes.int4.transform(SqlFunction { it }, { it }))
    open val int8: PgType<Long> = PgType(JavaPgTypes.int8.transform(SqlFunction { it }, { it }))
    open val float4: PgType<Float> = PgType(JavaPgTypes.float4.transform(SqlFunction { it }, { it }))
    open val float8: PgType<Double> = PgType(JavaPgTypes.float8.transform(SqlFunction { it }, { it }))

    // Collections - convert Java Map to Kotlin Map
    open val hstore: PgType<Map<String, String>> = PgType(JavaPgTypes.hstore.transform(
        SqlFunction { javaMap -> javaMap.toMap() },
        { kotlinMap -> kotlinMap.toMap(java.util.HashMap()) }
    ))

    // Forward all other types directly from Java
    open val numeric: PgType<java.math.BigDecimal> = PgType(JavaPgTypes.numeric)
    open val aclitem: PgType<dev.typr.foundations.data.AclItem> = PgType(JavaPgTypes.aclitem)
    open val anyarray: PgType<dev.typr.foundations.data.AnyArray> = PgType(JavaPgTypes.anyarray)
    open val bit: PgType<dev.typr.foundations.data.Bit> = PgType(JavaPgTypes.bit)
    open val varbit: PgType<dev.typr.foundations.data.Varbit> = PgType(JavaPgTypes.varbit)
    open val inet: PgType<dev.typr.foundations.data.Inet> = PgType(JavaPgTypes.inet)
    open val cidr: PgType<dev.typr.foundations.data.Cidr> = PgType(JavaPgTypes.cidr)
    open val macaddr: PgType<dev.typr.foundations.data.MacAddr> = PgType(JavaPgTypes.macaddr)
    open val macaddr8: PgType<dev.typr.foundations.data.MacAddr8> = PgType(JavaPgTypes.macaddr8)
    open val timestamptz: PgType<java.time.Instant> = PgType(JavaPgTypes.timestamptz)
    open val int2vector: PgType<dev.typr.foundations.data.Int2Vector> = PgType(JavaPgTypes.int2vector)
    open val json: PgType<dev.typr.foundations.data.Json> = PgType(JavaPgTypes.json)
    open val jsonb: PgType<dev.typr.foundations.data.Jsonb> = PgType(JavaPgTypes.jsonb)
    open val date: PgType<java.time.LocalDate> = PgType(JavaPgTypes.date)
    open val timestamp: PgType<java.time.LocalDateTime> = PgType(JavaPgTypes.timestamp)
    open val time: PgType<java.time.LocalTime> = PgType(JavaPgTypes.time)
    open val oid: PgType<dev.typr.foundations.data.Oid> = PgType(JavaPgTypes.oid)
    open val money: PgType<dev.typr.foundations.data.Money> = PgType(JavaPgTypes.money)
    open val name: PgType<String> = PgType(JavaPgTypes.name)
    open val timetz: PgType<java.time.OffsetTime> = PgType(JavaPgTypes.timetz)
    open val oidvector: PgType<dev.typr.foundations.data.OidVector> = PgType(JavaPgTypes.oidvector)
    open val interval: PgType<org.postgresql.util.PGInterval> = PgType(JavaPgTypes.interval)
    open val box: PgType<org.postgresql.geometric.PGbox> = PgType(JavaPgTypes.box)
    open val circle: PgType<org.postgresql.geometric.PGcircle> = PgType(JavaPgTypes.circle)
    open val line: PgType<org.postgresql.geometric.PGline> = PgType(JavaPgTypes.line)
    open val lseg: PgType<org.postgresql.geometric.PGlseg> = PgType(JavaPgTypes.lseg)
    open val path: PgType<org.postgresql.geometric.PGpath> = PgType(JavaPgTypes.path)
    open val point: PgType<org.postgresql.geometric.PGpoint> = PgType(JavaPgTypes.point)
    open val polygon: PgType<org.postgresql.geometric.PGpolygon> = PgType(JavaPgTypes.polygon)
    open val pgNodeTree: PgType<dev.typr.foundations.data.PgNodeTree> = PgType(JavaPgTypes.pgNodeTree)
    open val regclass: PgType<dev.typr.foundations.data.Regclass> = PgType(JavaPgTypes.regclass)
    open val regconfig: PgType<dev.typr.foundations.data.Regconfig> = PgType(JavaPgTypes.regconfig)
    open val regdictionary: PgType<dev.typr.foundations.data.Regdictionary> = PgType(JavaPgTypes.regdictionary)
    open val regnamespace: PgType<dev.typr.foundations.data.Regnamespace> = PgType(JavaPgTypes.regnamespace)
    open val regoper: PgType<dev.typr.foundations.data.Regoper> = PgType(JavaPgTypes.regoper)
    open val regoperator: PgType<dev.typr.foundations.data.Regoperator> = PgType(JavaPgTypes.regoperator)
    open val regproc: PgType<dev.typr.foundations.data.Regproc> = PgType(JavaPgTypes.regproc)
    open val regprocedure: PgType<dev.typr.foundations.data.Regprocedure> = PgType(JavaPgTypes.regprocedure)
    open val regrole: PgType<dev.typr.foundations.data.Regrole> = PgType(JavaPgTypes.regrole)
    open val regtype: PgType<dev.typr.foundations.data.Regtype> = PgType(JavaPgTypes.regtype)
    open val bpchar: PgType<String> = PgType(JavaPgTypes.bpchar)
    open val text: PgType<String> = PgType(JavaPgTypes.text)
    open val uuid: PgType<java.util.UUID> = PgType(JavaPgTypes.uuid)
    open val xid: PgType<dev.typr.foundations.data.Xid> = PgType(JavaPgTypes.xid)
    open val xml: PgType<dev.typr.foundations.data.Xml> = PgType(JavaPgTypes.xml)
    open val vector: PgType<dev.typr.foundations.data.Vector> = PgType(JavaPgTypes.vector)
    open val unknown: PgType<dev.typr.foundations.data.Unknown> = PgType(JavaPgTypes.unknown)
    open val bytea: PgType<ByteArray> = PgType(JavaPgTypes.bytea)
    open val int4range: PgType<dev.typr.foundations.data.Range<Int>> = PgType(JavaPgTypes.int4range)
    open val int8range: PgType<dev.typr.foundations.data.Range<Long>> = PgType(JavaPgTypes.int8range)
    open val numrange: PgType<dev.typr.foundations.data.Range<java.math.BigDecimal>> = PgType(JavaPgTypes.numrange)
    open val daterange: PgType<dev.typr.foundations.data.Range<java.time.LocalDate>> = PgType(JavaPgTypes.daterange)
    open val tsrange: PgType<dev.typr.foundations.data.Range<java.time.LocalDateTime>> = PgType(JavaPgTypes.tsrange)
    open val tstzrange: PgType<dev.typr.foundations.data.Range<java.time.Instant>> = PgType(JavaPgTypes.tstzrange)
    open val record: PgType<dev.typr.foundations.data.Record> = PgType(JavaPgTypes.record)
    open val smallserial: PgType<Short> = PgType(JavaPgTypes.smallserial.transform(SqlFunction { it }, { it }))

    // Parameterized methods
    open fun bitOf(n: Int): PgType<dev.typr.foundations.data.Bit> = PgType(JavaPgTypes.bitOf(n))

    open fun bpcharOf(length: Int): PgType<String> = PgType(JavaPgTypes.bpcharOf(length))

    open fun recordOf(sqlType: String): PgType<dev.typr.foundations.data.Record> = PgType(JavaPgTypes.recordOf(sqlType))

    open fun <E : Enum<E>> ofEnum(enumTypeName: String, fromString: (String) -> E): PgType<E> =
        PgType(JavaPgTypes.ofEnum(enumTypeName) { fromString(it) })

    open fun <E : Enum<E>> ofEnum(enumTypeName: String, values: Array<E>): PgType<E> =
        PgType(JavaPgTypes.ofEnum(enumTypeName, values))

    open fun <E> ofEnum(enumTypeName: String, values: Array<E>, name: java.util.function.Function<E, String>): PgType<E> =
        PgType(JavaPgTypes.ofEnum(enumTypeName, values, name))

    open fun <T> ofPgObject(
        sqlType: String,
        constructor: dev.typr.foundations.SqlFunction<String, T>,
        extractor: java.util.function.Function<T, String>,
        json: dev.typr.foundations.PgJson<T>
    ): PgType<T> = PgType(JavaPgTypes.ofPgObject(sqlType, constructor, extractor, json))

    open fun <T : org.postgresql.util.PGobject> pgObject(
        sqlType: String,
        clazz: Class<T>,
        json: dev.typr.foundations.PgJson<T>
    ): PgType<T> = PgType(JavaPgTypes.pgObject(sqlType, clazz, json))

    open fun <T : Comparable<T>> rangeType(
        sqlType: String,
        valueParser: dev.typr.foundations.SqlFunction<String, T>,
        rangeFactory: java.util.function.BiFunction<dev.typr.foundations.data.RangeBound<T>, dev.typr.foundations.data.RangeBound<T>, dev.typr.foundations.data.Range<T>>,
        json: dev.typr.foundations.PgJson<dev.typr.foundations.data.Range<T>>
    ): PgType<dev.typr.foundations.data.Range<T>> = PgType(JavaPgTypes.rangeType(sqlType, valueParser, rangeFactory, json))

    /** Build an ad-hoc composite PgType from a RowCodec. Read-only, for row constructors. */
    open fun <Row : Any> compositeOf(codec: RowCodecNamed<Row>): PgType<Row> =
        PgType(JavaPgTypes.compositeOf(codec.underlying))

    /** Build a named composite PgType from a RowCodec. Read-write, for CREATE TYPE declarations. */
    open fun <Row : Any> compositeOf(sqlType: String, codec: RowCodecNamed<Row>): PgType<Row> =
        PgType(JavaPgTypes.compositeOf(sqlType, codec.underlying))

    // JSON-encoded row types (json)

    open fun <Row : Any> jsonArrayEncoded(parser: RowCodec<Row>): PgType<Row> =
        PgType(JavaPgTypes.jsonArrayEncoded(parser.underlying))

    open fun <Row : Any> jsonArrayEncodedList(parser: RowCodec<Row>): PgType<List<Row>> =
        PgType(JavaPgTypes.jsonArrayEncodedList(parser.underlying))

    open fun <Row : Any> jsonObjectEncoded(parser: RowCodecNamed<Row>): PgType<Row> =
        PgType(JavaPgTypes.jsonObjectEncoded(parser.underlying))

    open fun <Row : Any> jsonObjectEncodedList(parser: RowCodecNamed<Row>): PgType<List<Row>> =
        PgType(JavaPgTypes.jsonObjectEncodedList(parser.underlying))

    // JSON-encoded row types (jsonb)

    open fun <Row : Any> jsonbArrayEncoded(parser: RowCodec<Row>): PgType<Row> =
        PgType(JavaPgTypes.jsonbArrayEncoded(parser.underlying))

    open fun <Row : Any> jsonbArrayEncodedList(parser: RowCodec<Row>): PgType<List<Row>> =
        PgType(JavaPgTypes.jsonbArrayEncodedList(parser.underlying))

    open fun <Row : Any> jsonbObjectEncoded(parser: RowCodecNamed<Row>): PgType<Row> =
        PgType(JavaPgTypes.jsonbObjectEncoded(parser.underlying))

    open fun <Row : Any> jsonbObjectEncodedList(parser: RowCodecNamed<Row>): PgType<List<Row>> =
        PgType(JavaPgTypes.jsonbObjectEncodedList(parser.underlying))

    companion object : PgTypes()
}

inline fun <reified E : Enum<E>> PgTypes.Companion.ofEnum(enumTypeName: String): PgType<E> =
    ofEnum(enumTypeName, enumValues<E>())
