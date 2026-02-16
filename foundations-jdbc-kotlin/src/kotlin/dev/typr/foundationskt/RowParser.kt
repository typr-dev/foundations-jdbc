package dev.typr.foundationskt

import java.sql.ResultSet
import java.util.Optional

/**
 * Kotlin wrapper for dev.typr.foundations.RowParser that provides Kotlin-native methods.
 *
 * This class has the same API surface as the Java RowParser but returns Kotlin types (T?)
 * instead of Java types (Optional<T>).
 */
open class RowParser<Row : Any>(open val underlying: dev.typr.foundations.RowParser<Row>) {

    companion object {
        /**
         * Create a type-safe builder for RowParser.
         */
        fun <Row : Any> builder(): RowParserBuilders.Builder0<Row> = RowParserBuilders.builder()

        /**
         * Create a type-safe named builder for RowParser.
         */
        fun <Row : Any> namedBuilder(): RowParserNamedBuilders.Builder0<Row> = RowParserNamedBuilders.builder()

        /**
         * Create a single-column row parser.
         */
        fun <T : Any> of(type: DbType<T>): RowParser<T> = RowParser(dev.typr.foundations.RowParser.of(type.underlying))
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
    fun <Row2 : Any> joined(other: RowParser<Row2>): RowParser<Pair<Row, Row2>> {
        val javaResult = underlying.joined(other.underlying)
        val converted = javaResult.to(Bijection.andToPair<Row, Row2>())
        return RowParser(converted)
    }

    /**
     * Compose with another parser for LEFT JOIN results.
     * Returns Pair<Row, Row2?> with nullable right side.
     */
    fun <Row2 : Any> leftJoined(other: RowParser<Row2>?): RowParser<Pair<Row, Row2?>> {
        val javaResult: dev.typr.foundations.RowParser<dev.typr.foundations.And<Row, Optional<Row2>>> =
            underlying.leftJoined(other?.underlying)
        val converted = javaResult.to(Bijection.leftJoinToNullable<Row, Row2>())
        return RowParser(converted)
    }

    /**
     * Compose with another parser for RIGHT JOIN results.
     * Returns Pair<Row?, Row2> with nullable left side.
     */
    fun <Row2 : Any> rightJoined(other: RowParser<Row2>): RowParser<Pair<Row?, Row2>> {
        val javaResult: dev.typr.foundations.RowParser<dev.typr.foundations.And<Optional<Row>, Row2>> =
            underlying.rightJoined(other.underlying)
        val converted = javaResult.to(Bijection.rightJoinToNullable<Row, Row2>())
        return RowParser(converted)
    }

    /**
     * Compose with another parser for FULL OUTER JOIN results.
     * Returns Pair<Row?, Row2?> with both sides nullable.
     */
    fun <Row2 : Any> fullJoined(other: RowParser<Row2>): RowParser<Pair<Row?, Row2?>> {
        val javaResult: dev.typr.foundations.RowParser<dev.typr.foundations.And<Optional<Row>, Optional<Row2>>> =
            underlying.fullJoined(other.underlying)
        val converted = javaResult.to(Bijection.fullJoinToNullable<Row, Row2>())
        return RowParser(converted)
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
 * Kotlin wrapper for dev.typr.foundations.RowParserNamed.
 * Adds columnNames() and columnList() accessors, and a no-argument jsonObject().
 */
class RowParserNamed<Row : Any>(
    override val underlying: dev.typr.foundations.RowParserNamed<Row>
) : RowParser<Row>(underlying) {

    val columnNames: List<String>
        get() = underlying.columnNames().toList()

    val columnList: Fragment
        get() = Fragment(underlying.columnList())

    fun jsonObject(): dev.typr.foundations.DbJson<Row> =
        dev.typr.foundations.DbJsonRow.jsonObject(underlying)
}
