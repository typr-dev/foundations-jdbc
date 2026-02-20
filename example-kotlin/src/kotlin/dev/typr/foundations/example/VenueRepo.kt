package dev.typr.foundations.example

import dev.typr.foundationskt.*
import java.sql.Connection

val allVenues: Operation.Query<List<Venue>> =
    sql { "SELECT ${venueCodec.columnList} FROM venue ORDER BY name" }
        .query(venueCodec.all())

fun venueById(id: VenueId): Operation.Query<Venue?> =
    sql { "SELECT ${venueCodec.columnList} FROM venue WHERE id = ${venueIdType(id)}" }
        .query(venueCodec.maxOne())

fun createVenue(venue: Venue): Operation.Query<Venue> {
    val cols = venueCodec.columnNames.filter { it != "id" }.joinToString(", ")
    val values = Fragment.row(venueCodec, venue, "id")
    return sql { "INSERT INTO venue ($cols) VALUES ($values) RETURNING ${venueCodec.columnList}" }
        .query(venueCodec.exactlyOne())
}

fun analyzeVenueQueries(conn: Connection): List<QueryAnalysis> = listOf(
    QueryAnalyzer.analyze(allVenues.named("allVenues"), conn),
    QueryAnalyzer.analyze(venueById(VenueId(0)).named("venueById"), conn),
    QueryAnalyzer.analyze(createVenue(Venue(VenueId(0), "", Address("", "", "", "", ""), 0, emptyList(), emptyMap())).named("createVenue"), conn),
).flatten()
