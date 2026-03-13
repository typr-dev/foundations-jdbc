package dev.typr.foundationskt

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
    fun <Row2 : Any> joined(other: RowCodec<Row2>): RowCodec<Pair<Row, Row2>> {
        val javaResult = underlying.joined(other.underlying)
        val converted = javaResult.to(Bijection.andToPair<Row, Row2>())
        return RowCodec(converted)
    }

    /**
     * Compose with another parser for LEFT JOIN results.
     * Returns Pair<Row, Row2?> with nullable right side.
     */
    fun <Row2 : Any> leftJoined(other: RowCodec<Row2>?): RowCodec<Pair<Row, Row2?>> {
        val javaResult: dev.typr.foundations.RowCodec<dev.typr.foundations.Tuple.Tuple2<Row, Optional<Row2>>> =
            underlying.leftJoined(other?.underlying)
        val converted = javaResult.to(Bijection.leftJoinToNullable<Row, Row2>())
        return RowCodec(converted)
    }

    /**
     * Compose with another parser for RIGHT JOIN results.
     * Returns Pair<Row?, Row2> with nullable left side.
     */
    fun <Row2 : Any> rightJoined(other: RowCodec<Row2>): RowCodec<Pair<Row?, Row2>> {
        val javaResult: dev.typr.foundations.RowCodec<dev.typr.foundations.Tuple.Tuple2<Optional<Row>, Row2>> =
            underlying.rightJoined(other.underlying)
        val converted = javaResult.to(Bijection.rightJoinToNullable<Row, Row2>())
        return RowCodec(converted)
    }

    /**
     * Compose with another parser for FULL OUTER JOIN results.
     * Returns Pair<Row?, Row2?> with both sides nullable.
     */
    fun <Row2 : Any> fullJoined(other: RowCodec<Row2>): RowCodec<Pair<Row?, Row2?>> {
        val javaResult: dev.typr.foundations.RowCodec<dev.typr.foundations.Tuple.Tuple2<Optional<Row>, Optional<Row2>>> =
            underlying.fullJoined(other.underlying)
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

    fun columnList(alias: String): Fragment = Fragment(underlying.columnList(alias))

    fun <Row2 : Any> join(other: RowCodecNamed<Row2>): RowCodecNamed<Pair<Row, Row2>> {
        val javaJoined = underlying.join(other.underlying)
        val converted = javaJoined.to(Bijection.andToPair<Row, Row2>())
        return RowCodecNamed(converted)
    }

    fun <Row2 : Any> to(forward: (Row) -> Row2, backward: (Row2) -> Row): RowCodecNamed<Row2> =
        RowCodecNamed(underlying.to(dev.typr.foundations.Bijection.of(
            { forward(it) }, { backward(it) })))

    fun jsonObject(): dev.typr.foundations.DbJson<Row> =
        dev.typr.foundations.DbJsonRow.jsonObject(underlying)
}
