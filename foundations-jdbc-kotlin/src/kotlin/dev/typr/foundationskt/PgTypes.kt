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
    open val hstore = PgType(JavaPgTypes.hstore.transform(
        SqlFunction { javaMap -> javaMap.toMap() },
        { kotlinMap -> kotlinMap.toMap(java.util.HashMap()) }
    ))

    // Forward all other types directly from Java
    open val numeric = PgType(JavaPgTypes.numeric)
    open val aclitem = PgType(JavaPgTypes.aclitem)
    open val anyarray = PgType(JavaPgTypes.anyarray)
    open val bit = PgType(JavaPgTypes.bit)
    open val varbit = PgType(JavaPgTypes.varbit)
    open val inet = PgType(JavaPgTypes.inet)
    open val cidr = PgType(JavaPgTypes.cidr)
    open val macaddr = PgType(JavaPgTypes.macaddr)
    open val macaddr8 = PgType(JavaPgTypes.macaddr8)
    open val timestamptz = PgType(JavaPgTypes.timestamptz)
    open val int2vector = PgType(JavaPgTypes.int2vector)
    open val json = PgType(JavaPgTypes.json)
    open val jsonb = PgType(JavaPgTypes.jsonb)
    open val date = PgType(JavaPgTypes.date)
    open val timestamp = PgType(JavaPgTypes.timestamp)
    open val time = PgType(JavaPgTypes.time)
    open val oid = PgType(JavaPgTypes.oid)
    open val money = PgType(JavaPgTypes.money)
    open val name = PgType(JavaPgTypes.name)
    open val timetz = PgType(JavaPgTypes.timetz)
    open val oidvector = PgType(JavaPgTypes.oidvector)
    open val interval = PgType(JavaPgTypes.interval)
    open val box = PgType(JavaPgTypes.box)
    open val circle = PgType(JavaPgTypes.circle)
    open val line = PgType(JavaPgTypes.line)
    open val lseg = PgType(JavaPgTypes.lseg)
    open val path = PgType(JavaPgTypes.path)
    open val point = PgType(JavaPgTypes.point)
    open val polygon = PgType(JavaPgTypes.polygon)
    open val pgNodeTree = PgType(JavaPgTypes.pgNodeTree)
    open val regclass = PgType(JavaPgTypes.regclass)
    open val regconfig = PgType(JavaPgTypes.regconfig)
    open val regdictionary = PgType(JavaPgTypes.regdictionary)
    open val regnamespace = PgType(JavaPgTypes.regnamespace)
    open val regoper = PgType(JavaPgTypes.regoper)
    open val regoperator = PgType(JavaPgTypes.regoperator)
    open val regproc = PgType(JavaPgTypes.regproc)
    open val regprocedure = PgType(JavaPgTypes.regprocedure)
    open val regrole = PgType(JavaPgTypes.regrole)
    open val regtype = PgType(JavaPgTypes.regtype)
    open val bpchar = PgType(JavaPgTypes.bpchar)
    open val text = PgType(JavaPgTypes.text)
    open val uuid = PgType(JavaPgTypes.uuid)
    open val xid = PgType(JavaPgTypes.xid)
    open val xml = PgType(JavaPgTypes.xml)
    open val vector = PgType(JavaPgTypes.vector)
    open val unknown = PgType(JavaPgTypes.unknown)
    open val bytea = PgType(JavaPgTypes.bytea)
    open val int4range = PgType(JavaPgTypes.int4range)
    open val int8range = PgType(JavaPgTypes.int8range)
    open val numrange = PgType(JavaPgTypes.numrange)
    open val daterange = PgType(JavaPgTypes.daterange)
    open val tsrange = PgType(JavaPgTypes.tsrange)
    open val tstzrange = PgType(JavaPgTypes.tstzrange)
    open val record = PgType(JavaPgTypes.record)
    open val smallserial: PgType<Short> = PgType(JavaPgTypes.smallserial.transform(SqlFunction { it }, { it }))

    // Parameterized methods
    open fun bit(n: Int) = PgType(JavaPgTypes.bit(n))

    open fun bpchar(length: Int) = PgType(JavaPgTypes.bpchar(length))

    open fun record(sqlType: String) = PgType(JavaPgTypes.record(sqlType))

    open fun <E : Enum<E>> ofEnum(enumTypeName: String, fromString: (String) -> E) =
        PgType(JavaPgTypes.ofEnum(enumTypeName) { fromString(it) })

    open fun <T> ofPgObject(
        sqlType: String,
        constructor: dev.typr.foundations.SqlFunction<String, T>,
        extractor: java.util.function.Function<T, String>,
        json: dev.typr.foundations.PgJson<T>
    ) = PgType(JavaPgTypes.ofPgObject(sqlType, constructor, extractor, json))

    open fun <T : org.postgresql.util.PGobject> pgObject(
        sqlType: String,
        clazz: Class<T>,
        json: dev.typr.foundations.PgJson<T>
    ) = PgType(JavaPgTypes.pgObject(sqlType, clazz, json))

    open fun <T : Comparable<T>> rangeType(
        sqlType: String,
        valueParser: dev.typr.foundations.SqlFunction<String, T>,
        rangeFactory: java.util.function.BiFunction<dev.typr.foundations.data.RangeBound<T>, dev.typr.foundations.data.RangeBound<T>, dev.typr.foundations.data.Range<T>>,
        json: dev.typr.foundations.PgJson<dev.typr.foundations.data.Range<T>>
    ) = PgType(JavaPgTypes.rangeType(sqlType, valueParser, rangeFactory, json))

    /** Build an ad-hoc composite PgType from a RowCodec. Read-only, for row constructors. */
    open fun <Row : Any> compositeOf(codec: RowCodecNamed<Row>) =
        PgType(JavaPgTypes.compositeOf(codec.underlying))

    /** Build a named composite PgType from a RowCodec. Read-write, for CREATE TYPE declarations. */
    open fun <Row : Any> compositeOf(sqlType: String, codec: RowCodecNamed<Row>) =
        PgType(JavaPgTypes.compositeOf(sqlType, codec.underlying))

    // JSON-encoded row types (json)

    open fun <Row : Any> jsonArrayEncoded(parser: RowCodec<Row>) =
        PgType<Row>(JavaPgTypes.jsonArrayEncoded(parser.underlying))

    open fun <Row : Any> jsonArrayEncodedList(parser: RowCodec<Row>) =
        PgType<List<Row>>(JavaPgTypes.jsonArrayEncodedList(parser.underlying))

    open fun <Row : Any> jsonObjectEncoded(parser: RowCodecNamed<Row>) =
        PgType<Row>(JavaPgTypes.jsonObjectEncoded(parser.underlying))

    open fun <Row : Any> jsonObjectEncodedList(parser: RowCodecNamed<Row>) =
        PgType<List<Row>>(JavaPgTypes.jsonObjectEncodedList(parser.underlying))

    // JSON-encoded row types (jsonb)

    open fun <Row : Any> jsonbArrayEncoded(parser: RowCodec<Row>) =
        PgType<Row>(JavaPgTypes.jsonbArrayEncoded(parser.underlying))

    open fun <Row : Any> jsonbArrayEncodedList(parser: RowCodec<Row>) =
        PgType<List<Row>>(JavaPgTypes.jsonbArrayEncodedList(parser.underlying))

    open fun <Row : Any> jsonbObjectEncoded(parser: RowCodecNamed<Row>) =
        PgType<Row>(JavaPgTypes.jsonbObjectEncoded(parser.underlying))

    open fun <Row : Any> jsonbObjectEncodedList(parser: RowCodecNamed<Row>) =
        PgType<List<Row>>(JavaPgTypes.jsonbObjectEncodedList(parser.underlying))

    companion object : PgTypes()
}
