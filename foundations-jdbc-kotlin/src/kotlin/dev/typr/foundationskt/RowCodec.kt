package dev.typr.foundationskt

import dev.typr.foundations.Tuple
import java.sql.ResultSet
import java.util.Optional

/**
 * Kotlin wrapper for dev.typr.foundations.RowCodec that provides Kotlin-native methods.
 *
 * This class has the same API surface as the Java RowCodec but returns Kotlin types (T?)
 * instead of Java types (Optional<T>).
 */
open class RowCodec<Row : Any>(open val underlying: dev.typr.foundations.RowCodec<Row>) {

    companion object {
        /**
         * Create a type-safe builder for RowCodec.
         */
        fun <Row : Any> builder(): RowCodecBuilders.Builder0<Row> = RowCodecBuilders.builder()

        /**
         * Create a type-safe named builder for RowCodec.
         */
        fun <Row : Any> namedBuilder(): RowCodecNamedBuilders.Builder0<Row> = RowCodecNamedBuilders.builder()

        /**
         * Create a single-column row parser.
         */
        fun <T : Any> of(type: DbType<T>): RowCodec<T> = RowCodec(dev.typr.foundations.RowCodec.of(type.underlying))

        /** Two-column row codec yielding a Kotlin [Pair]. */
        fun <T0, T1> of(t0: DbType<T0>, t1: DbType<T1>): RowCodec<Pair<T0, T1>> {
            val javaResult: dev.typr.foundations.RowCodec<Tuple.Tuple2<T0, T1>> =
                dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying)
            return RowCodec(javaResult.to(Bijection.andToPair<T0, T1>()))
        }

        /** Three-column row codec yielding a Kotlin [Triple]. */
        fun <T0, T1, T2> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>): RowCodec<Triple<T0, T1, T2>> {
            val javaResult: dev.typr.foundations.RowCodec<Tuple.Tuple3<T0, T1, T2>> =
                dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying)
            return RowCodec(javaResult.to(Bijection.tuple3ToTriple<T0, T1, T2>()))
        }

        fun <T0, T1, T2, T3> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>): RowCodec<Tuple.Tuple4<T0, T1, T2, T3>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying))

        fun <T0, T1, T2, T3, T4> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>): RowCodec<Tuple.Tuple5<T0, T1, T2, T3, T4>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying))

        fun <T0, T1, T2, T3, T4, T5> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>): RowCodec<Tuple.Tuple6<T0, T1, T2, T3, T4, T5>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>): RowCodec<Tuple.Tuple7<T0, T1, T2, T3, T4, T5, T6>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>): RowCodec<Tuple.Tuple8<T0, T1, T2, T3, T4, T5, T6, T7>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>): RowCodec<Tuple.Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>): RowCodec<Tuple.Tuple10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>): RowCodec<Tuple.Tuple11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>): RowCodec<Tuple.Tuple12<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>, t12: DbType<T12>): RowCodec<Tuple.Tuple13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying, t12.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>, t12: DbType<T12>, t13: DbType<T13>): RowCodec<Tuple.Tuple14<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying, t12.underlying, t13.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>, t12: DbType<T12>, t13: DbType<T13>, t14: DbType<T14>): RowCodec<Tuple.Tuple15<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying, t12.underlying, t13.underlying, t14.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>, t12: DbType<T12>, t13: DbType<T13>, t14: DbType<T14>, t15: DbType<T15>): RowCodec<Tuple.Tuple16<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying, t12.underlying, t13.underlying, t14.underlying, t15.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>, t12: DbType<T12>, t13: DbType<T13>, t14: DbType<T14>, t15: DbType<T15>, t16: DbType<T16>): RowCodec<Tuple.Tuple17<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying, t12.underlying, t13.underlying, t14.underlying, t15.underlying, t16.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>, t12: DbType<T12>, t13: DbType<T13>, t14: DbType<T14>, t15: DbType<T15>, t16: DbType<T16>, t17: DbType<T17>): RowCodec<Tuple.Tuple18<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying, t12.underlying, t13.underlying, t14.underlying, t15.underlying, t16.underlying, t17.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>, t12: DbType<T12>, t13: DbType<T13>, t14: DbType<T14>, t15: DbType<T15>, t16: DbType<T16>, t17: DbType<T17>, t18: DbType<T18>): RowCodec<Tuple.Tuple19<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying, t12.underlying, t13.underlying, t14.underlying, t15.underlying, t16.underlying, t17.underlying, t18.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>, t12: DbType<T12>, t13: DbType<T13>, t14: DbType<T14>, t15: DbType<T15>, t16: DbType<T16>, t17: DbType<T17>, t18: DbType<T18>, t19: DbType<T19>): RowCodec<Tuple.Tuple20<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying, t12.underlying, t13.underlying, t14.underlying, t15.underlying, t16.underlying, t17.underlying, t18.underlying, t19.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>, t12: DbType<T12>, t13: DbType<T13>, t14: DbType<T14>, t15: DbType<T15>, t16: DbType<T16>, t17: DbType<T17>, t18: DbType<T18>, t19: DbType<T19>, t20: DbType<T20>): RowCodec<Tuple.Tuple21<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying, t12.underlying, t13.underlying, t14.underlying, t15.underlying, t16.underlying, t17.underlying, t18.underlying, t19.underlying, t20.underlying))

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> of(t0: DbType<T0>, t1: DbType<T1>, t2: DbType<T2>, t3: DbType<T3>, t4: DbType<T4>, t5: DbType<T5>, t6: DbType<T6>, t7: DbType<T7>, t8: DbType<T8>, t9: DbType<T9>, t10: DbType<T10>, t11: DbType<T11>, t12: DbType<T12>, t13: DbType<T13>, t14: DbType<T14>, t15: DbType<T15>, t16: DbType<T16>, t17: DbType<T17>, t18: DbType<T18>, t19: DbType<T19>, t20: DbType<T20>, t21: DbType<T21>): RowCodec<Tuple.Tuple22<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> =
            RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying, t9.underlying, t10.underlying, t11.underlying, t12.underlying, t13.underlying, t14.underlying, t15.underlying, t16.underlying, t17.underlying, t18.underlying, t19.underlying, t20.underlying, t21.underlying))

        /**
         * Create a single-column named row codec.
         */
        fun <T : Any> ofNamed(name: String, type: DbType<T>): RowCodecNamed<T> =
            RowCodecNamed(dev.typr.foundations.RowCodec.ofNamed(name, type.underlying))
    }

    /**
     * Parse all rows from a ResultSet.
     * Returns Kotlin List instead of java.util.List.
     */
    fun all(): ResultSetParser<List<Row>> {
        return ResultSetParser(underlying.all().map { it.toList() })
    }

    /**
     * Compose with another parser for INNER JOIN results.
     * Returns Pair<Row, Row2>.
     */
    fun <Row2 : Any> join(other: RowCodec<Row2>): RowCodec<Pair<Row, Row2>> {
        val javaResult = underlying.join(other.underlying)
        val converted = javaResult.to(Bijection.andToPair<Row, Row2>())
        return RowCodec(converted)
    }

    /**
     * Compose with another parser for LEFT JOIN results.
     * Returns Pair<Row, Row2?> with nullable right side.
     */
    fun <Row2 : Any> leftJoin(other: RowCodec<Row2>?): RowCodec<Pair<Row, Row2?>> {
        val javaResult: dev.typr.foundations.RowCodec<dev.typr.foundations.Tuple.Tuple2<Row, Optional<Row2>>> =
            underlying.leftJoin(other?.underlying)
        val converted = javaResult.to(Bijection.leftJoinToNullable<Row, Row2>())
        return RowCodec(converted)
    }

    /**
     * Compose with another parser for RIGHT JOIN results.
     * Returns Pair<Row?, Row2> with nullable left side.
     */
    fun <Row2 : Any> rightJoin(other: RowCodec<Row2>): RowCodec<Pair<Row?, Row2>> {
        val javaResult: dev.typr.foundations.RowCodec<dev.typr.foundations.Tuple.Tuple2<Optional<Row>, Row2>> =
            underlying.rightJoin(other.underlying)
        val converted = javaResult.to(Bijection.rightJoinToNullable<Row, Row2>())
        return RowCodec(converted)
    }

    /**
     * Compose with another parser for FULL OUTER JOIN results.
     * Returns Pair<Row?, Row2?> with both sides nullable.
     */
    fun <Row2 : Any> fullJoin(other: RowCodec<Row2>): RowCodec<Pair<Row?, Row2?>> {
        val javaResult: dev.typr.foundations.RowCodec<dev.typr.foundations.Tuple.Tuple2<Optional<Row>, Optional<Row2>>> =
            underlying.fullJoin(other.underlying)
        val converted = javaResult.to(Bijection.fullJoinToNullable<Row, Row2>())
        return RowCodec(converted)
    }

    /**
     * Parse exactly one row from a ResultSet.
     * Returns Row directly (throws if not exactly one row).
     */
    fun exactlyOne(): ResultSetParser<Row> {
        return ResultSetParser(underlying.exactlyOne())
    }

    /**
     * Parse the first row from a ResultSet or null if empty.
     * Returns Row? instead of Optional<Row>.
     */
    fun first(): ResultSetParser<Row?> {
        return ResultSetParser(underlying.first().map { it.orElse(null) })
    }

    /**
     * Parse at most one row from a ResultSet or null.
     * Returns Row? instead of Optional<Row>.
     */
    fun maxOne(): ResultSetParser<Row?> {
        return ResultSetParser(underlying.maxOne().map { it.orElse(null) })
    }

    /**
     * Parse a single row from the current position in ResultSet.
     */
    fun parse(rs: ResultSet): Row = underlying.parse(rs)

    /**
     * Create a PgText encoder for streaming COPY operations.
     */
    fun pgText(): dev.typr.foundations.PgText<Row> =
        dev.typr.foundations.PgText.from(underlying)

    /**
     * Create a DbJson codec that encodes rows as JSON arrays.
     */
    fun jsonArray(): dev.typr.foundations.DbJson<Row> =
        dev.typr.foundations.DbJsonRow.jsonArray(underlying)

    /**
     * Create a DbJson codec that encodes rows as JSON objects with named fields.
     */
    fun jsonObject(columnNames: List<String>): dev.typr.foundations.DbJson<Row> =
        dev.typr.foundations.DbJsonRow.jsonObject(underlying, columnNames)
}

/**
 * Kotlin wrapper for dev.typr.foundations.RowCodecNamed.
 * Adds columnNames() and columnList() accessors, and a no-argument jsonObject().
 */
class RowCodecNamed<Row : Any>(
    override val underlying: dev.typr.foundations.RowCodecNamed<Row>
) : RowCodec<Row>(underlying) {

    val columnNames: List<String>
        get() = underlying.columnNames().toList()

    val columnList: Fragment
        get() = Fragment(underlying.columnList())

    /**
     * Return a copy of this codec where every column name is prefixed with `alias + "."`.
     * Apply before a join to keep [columnList] unambiguous:
     *
     * ```kotlin
     * empCodec.aliased("e").leftJoin(deptCodec.aliased("d"))
     * ```
     */
    fun aliased(alias: String): RowCodecNamed<Row> = RowCodecNamed(underlying.aliased(alias))

    /** Inner join that preserves column names. Returns a [RowCodecNamed]. */
    fun <Row2 : Any> join(other: RowCodecNamed<Row2>): RowCodecNamed<Pair<Row, Row2>> {
        val javaJoined = underlying.join(other.underlying)
        val converted = javaJoined.to(Bijection.andToPair<Row, Row2>())
        return RowCodecNamed(converted)
    }

    /**
     * Left join that preserves column names. Right-side columns are wrapped in .opt() so a row
     * with all-null right-side columns decodes as null. Returns a [RowCodecNamed] so
     * `columnList`, `Fragment.insertInto`, and JSON encoding keep working on the combined codec.
     */
    fun <Row2 : Any> leftJoin(other: RowCodecNamed<Row2>): RowCodecNamed<Pair<Row, Row2?>> {
        val javaJoined = underlying.leftJoin(other.underlying)
        val converted = javaJoined.to(Bijection.leftJoinToNullable<Row, Row2>())
        return RowCodecNamed(converted)
    }

    /**
     * Right join that preserves column names. Left-side columns are wrapped in .opt() so a row
     * with all-null left-side columns decodes as null.
     */
    fun <Row2 : Any> rightJoin(other: RowCodecNamed<Row2>): RowCodecNamed<Pair<Row?, Row2>> {
        val javaJoined = underlying.rightJoin(other.underlying)
        val converted = javaJoined.to(Bijection.rightJoinToNullable<Row, Row2>())
        return RowCodecNamed(converted)
    }

    /** Full outer join that preserves column names. Both sides are wrapped in .opt(). */
    fun <Row2 : Any> fullJoin(other: RowCodecNamed<Row2>): RowCodecNamed<Pair<Row?, Row2?>> {
        val javaJoined = underlying.fullJoin(other.underlying)
        val converted = javaJoined.to(Bijection.fullJoinToNullable<Row, Row2>())
        return RowCodecNamed(converted)
    }

    fun <Row2 : Any> to(forward: (Row) -> Row2, backward: (Row2) -> Row): RowCodecNamed<Row2> =
        RowCodecNamed(underlying.to(dev.typr.foundations.Bijection.of(
            { forward(it) }, { backward(it) })))

    fun jsonObject(): dev.typr.foundations.DbJson<Row> =
        dev.typr.foundations.DbJsonRow.jsonObject(underlying)
}
