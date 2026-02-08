package dev.typr.kotlinfoundations

import dev.typr.foundations.And
import dev.typr.foundations.DbType
import java.sql.ResultSet
import java.util.Optional

/**
 * Kotlin wrapper for dev.typr.foundations.RowParser that provides Kotlin-native methods.
 *
 * This class has the same API surface as the Java RowParser but returns Kotlin types (T?)
 * instead of Java types (Optional<T>).
 */
class RowParser<Row : Any>(val underlying: dev.typr.foundations.RowParser<Row>) {

    companion object {
        /**
         * Create a type-safe builder for RowParser.
         */
        fun <Row : Any> builder(): RowParserBuilders.Builder0<Row> = RowParserBuilders.builder()

        /**
         * Create a single-column row parser.
         */
        fun <T : Any> of(type: DbType<T>): RowParser<T> = RowParser(dev.typr.foundations.RowParser.of(type))
    }

    /**
     * Parse all rows from a ResultSet.
     * Returns Kotlin List instead of java.util.List.
     */
    fun all(): ResultSetParser<List<Row>> {
        val javaParser = underlying.all()
        return ResultSetParser(dev.typr.foundations.ResultSetParser { rs -> javaParser.apply(rs).toList() })
    }

    /**
     * Compose with another parser for INNER JOIN results.
     * Returns And<Row, Row2>.
     */
    fun <Row2 : Any> joined(other: RowParser<Row2>): RowParser<And<Row, Row2>> {
        return RowParser(underlying.joined(other.underlying))
    }

    /**
     * Compose with another parser for LEFT JOIN results.
     * Returns And<Row, Row2?> with nullable right side.
     */
    fun <Row2 : Any> leftJoined(other: RowParser<Row2>?): RowParser<And<Row, Row2?>> {
        val javaResult: dev.typr.foundations.RowParser<And<Row, Optional<Row2>>> =
            underlying.leftJoined(other?.underlying)
        val converted = javaResult.to(leftJoinToNullable<Row, Row2>())
        return RowParser(converted)
    }

    /**
     * Compose with another parser for RIGHT JOIN results.
     * Returns And<Row?, Row2> with nullable left side.
     */
    fun <Row2 : Any> rightJoined(other: RowParser<Row2>): RowParser<And<Row?, Row2>> {
        val javaResult: dev.typr.foundations.RowParser<And<Optional<Row>, Row2>> =
            underlying.rightJoined(other.underlying)
        val converted = javaResult.to(rightJoinToNullable<Row, Row2>())
        return RowParser(converted)
    }

    /**
     * Compose with another parser for FULL OUTER JOIN results.
     * Returns And<Row?, Row2?> with both sides nullable.
     */
    fun <Row2 : Any> fullJoined(other: RowParser<Row2>): RowParser<And<Row?, Row2?>> {
        val javaResult: dev.typr.foundations.RowParser<And<Optional<Row>, Optional<Row2>>> =
            underlying.fullJoined(other.underlying)
        val converted = javaResult.to(fullJoinToNullable<Row, Row2>())
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
        val javaParser = dev.typr.foundations.ResultSetParser.First(underlying)
        return ResultSetParser(dev.typr.foundations.ResultSetParser { rs -> javaParser.apply(rs).orElse(null) })
    }

    /**
     * Parse the first row from a ResultSet or null if empty.
     * Alias for first() to match Java API.
     */
    fun firstOrNull(): ResultSetParser<Row?> = first()

    /**
     * Parse at most one row from a ResultSet or null.
     * Returns Row? instead of Optional<Row>.
     */
    fun maxOne(): ResultSetParser<Row?> {
        val javaParser = dev.typr.foundations.ResultSetParser.MaxOne(underlying)
        return ResultSetParser(dev.typr.foundations.ResultSetParser { rs -> javaParser.apply(rs).orElse(null) })
    }

    /**
     * Parse at most one row from a ResultSet or null.
     * Alias for maxOne() to match Kotlin conventions.
     */
    fun maxOneOrNull(): ResultSetParser<Row?> = maxOne()

    /**
     * Parse a single row from the current position in ResultSet.
     */
    fun parse(rs: ResultSet): Row = underlying.parse(rs)

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
 * Convert a Java RowParser to a Kotlin RowParser.
 */
fun <Row : Any> dev.typr.foundations.RowParser<Row>.asKotlin(): RowParser<Row> {
    return RowParser(this)
}
