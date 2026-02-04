package dev.typr.foundations.kotlin

import dev.typr.foundations.PgType
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
    open val bool: PgType<Boolean> = JavaPgTypes.bool.bimap(SqlFunction { it }, { it })
    open val int2: PgType<Short> = JavaPgTypes.int2.bimap(SqlFunction { it }, { it })
    open val smallint: PgType<Short> = JavaPgTypes.smallint.bimap(SqlFunction { it }, { it })
    open val int4: PgType<Int> = JavaPgTypes.int4.bimap(SqlFunction { it }, { it })
    open val int8: PgType<Long> = JavaPgTypes.int8.bimap(SqlFunction { it }, { it })
    open val float4: PgType<Float> = JavaPgTypes.float4.bimap(SqlFunction { it }, { it })
    open val float8: PgType<Double> = JavaPgTypes.float8.bimap(SqlFunction { it }, { it })

    // Collections - convert Java Map to Kotlin Map
    open val hstore: PgType<Map<String, String>> = JavaPgTypes.hstore.bimap(
        SqlFunction { javaMap -> javaMap.toMap() },
        { kotlinMap -> kotlinMap.toMap(java.util.HashMap()) }
    )

    // Forward all other types directly from Java
    open val numeric = JavaPgTypes.numeric
    open val numericArray = JavaPgTypes.numericArray
    open val boolArray = JavaPgTypes.boolArray
    open val int2Array = JavaPgTypes.int2Array
    open val smallintArray = int2Array
    open val int4Array = JavaPgTypes.int4Array
    open val int8Array = JavaPgTypes.int8Array
    open val float4Array = JavaPgTypes.float4Array
    open val float8Array = JavaPgTypes.float8Array
    open val aclitem = JavaPgTypes.aclitem
    open val aclitemArray = JavaPgTypes.aclitemArray
    open val anyarray = JavaPgTypes.anyarray
    open val anyarrayArray = JavaPgTypes.anyarrayArray
    open val boolArrayUnboxed = JavaPgTypes.boolArrayUnboxed
    open val float8ArrayUnboxed = JavaPgTypes.float8ArrayUnboxed
    open val float4ArrayUnboxed = JavaPgTypes.float4ArrayUnboxed
    open val inet = JavaPgTypes.inet
    open val inetArray = JavaPgTypes.inetArray
    open val cidr = JavaPgTypes.cidr
    open val cidrArray = JavaPgTypes.cidrArray
    open val macaddr = JavaPgTypes.macaddr
    open val macaddrArray = JavaPgTypes.macaddrArray
    open val macaddr8 = JavaPgTypes.macaddr8
    open val macaddr8Array = JavaPgTypes.macaddr8Array
    open val timestamptz = JavaPgTypes.timestamptz
    open val timestamptzArray = JavaPgTypes.timestamptzArray
    open val int2vector = JavaPgTypes.int2vector
    open val int2vectorArray = JavaPgTypes.int2vectorArray
    open val int4ArrayUnboxed = JavaPgTypes.int4ArrayUnboxed
    open val json = JavaPgTypes.json
    open val jsonArray = JavaPgTypes.jsonArray
    open val jsonb = JavaPgTypes.jsonb
    open val jsonbArray = JavaPgTypes.jsonbArray
    open val date = JavaPgTypes.date
    open val timestamp = JavaPgTypes.timestamp
    open val timestampArray = JavaPgTypes.timestampArray
    open val dateArray = JavaPgTypes.dateArray
    open val time = JavaPgTypes.time
    open val timeArray = JavaPgTypes.timeArray
    open val int8ArrayUnboxed = JavaPgTypes.int8ArrayUnboxed
    open val oid = JavaPgTypes.oid
    open val oidArray = JavaPgTypes.oidArray
    open val money = JavaPgTypes.money
    open val moneyArray = JavaPgTypes.moneyArray
    open val name = JavaPgTypes.name
    open val nameArray = JavaPgTypes.nameArray
    open val timetz = JavaPgTypes.timetz
    open val timetzArray = JavaPgTypes.timetzArray
    open val oidvector = JavaPgTypes.oidvector
    open val oidvectorArray = JavaPgTypes.oidvectorArray
    open val interval = JavaPgTypes.interval
    open val intervalArray = JavaPgTypes.intervalArray
    open val box = JavaPgTypes.box
    open val boxArray = JavaPgTypes.boxArray
    open val circle = JavaPgTypes.circle
    open val circleArray = JavaPgTypes.circleArray
    open val line = JavaPgTypes.line
    open val lineArray = JavaPgTypes.lineArray
    open val lseg = JavaPgTypes.lseg
    open val lsegArray = JavaPgTypes.lsegArray
    open val path = JavaPgTypes.path
    open val pathArray = JavaPgTypes.pathArray
    open val point = JavaPgTypes.point
    open val pointArray = JavaPgTypes.pointArray
    open val polygon = JavaPgTypes.polygon
    open val polygonArray = JavaPgTypes.polygonArray
    open val pgNodeTree = JavaPgTypes.pgNodeTree
    open val pgNodeTreeArray = JavaPgTypes.pgNodeTreeArray
    open val regclass = JavaPgTypes.regclass
    open val regclassArray = JavaPgTypes.regclassArray
    open val regconfig = JavaPgTypes.regconfig
    open val regconfigArray = JavaPgTypes.regconfigArray
    open val regdictionary = JavaPgTypes.regdictionary
    open val regdictionaryArray = JavaPgTypes.regdictionaryArray
    open val regnamespace = JavaPgTypes.regnamespace
    open val regnamespaceArray = JavaPgTypes.regnamespaceArray
    open val regoper = JavaPgTypes.regoper
    open val regoperArray = JavaPgTypes.regoperArray
    open val regoperator = JavaPgTypes.regoperator
    open val regoperatorArray = JavaPgTypes.regoperatorArray
    open val regproc = JavaPgTypes.regproc
    open val regprocArray = JavaPgTypes.regprocArray
    open val regprocedure = JavaPgTypes.regprocedure
    open val regprocedureArray = JavaPgTypes.regprocedureArray
    open val regrole = JavaPgTypes.regrole
    open val regroleArray = JavaPgTypes.regroleArray
    open val regtype = JavaPgTypes.regtype
    open val regtypeArray = JavaPgTypes.regtypeArray
    open val int2ArrayUnboxed = JavaPgTypes.int2ArrayUnboxed
    open val smallintArrayUnboxed = JavaPgTypes.smallintArrayUnboxed
    open val bpchar = JavaPgTypes.bpchar
    open val text = JavaPgTypes.text
    open val bpcharArray = JavaPgTypes.bpcharArray
    open val textArray = JavaPgTypes.textArray
    open val uuid = JavaPgTypes.uuid
    open val uuidArray = JavaPgTypes.uuidArray
    open val xid = JavaPgTypes.xid
    open val xidArray = JavaPgTypes.xidArray
    open val xml = JavaPgTypes.xml
    open val xmlArray = JavaPgTypes.xmlArray
    open val vector = JavaPgTypes.vector
    open val vectorArray = JavaPgTypes.vectorArray
    open val unknown = JavaPgTypes.unknown
    open val unknownArray = JavaPgTypes.unknownArray
    open val bytea = JavaPgTypes.bytea
    open val int4range = JavaPgTypes.int4range
    open val int4rangeArray = JavaPgTypes.int4rangeArray
    open val int8range = JavaPgTypes.int8range
    open val int8rangeArray = JavaPgTypes.int8rangeArray
    open val numrange = JavaPgTypes.numrange
    open val numrangeArray = JavaPgTypes.numrangeArray
    open val daterange = JavaPgTypes.daterange
    open val daterangeArray = JavaPgTypes.daterangeArray
    open val tsrange = JavaPgTypes.tsrange
    open val tsrangeArray = JavaPgTypes.tsrangeArray
    open val tstzrange = JavaPgTypes.tstzrange
    open val tstzrangeArray = JavaPgTypes.tstzrangeArray
    open val record = JavaPgTypes.record
    open val recordArray = JavaPgTypes.recordArray

    companion object : PgTypes()
}
