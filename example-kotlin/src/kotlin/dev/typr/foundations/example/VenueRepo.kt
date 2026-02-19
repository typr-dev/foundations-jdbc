package dev.typr.foundations.example

import dev.typr.foundationskt.*
import java.sql.Connection

val allVenues: Operation.Query<List<Venue>> =
    Sql { "SELECT ${venueParser.columnList} FROM venue ORDER BY name" }
        .query(venueParser.all())

fun venueById(id: VenueId): Operation.Query<Venue?> =
    Sql { "SELECT ${venueParser.columnList} FROM venue WHERE id = ${venueIdType(id)}" }
        .query(venueParser.maxOne())

fun createVenue(venue: Venue): Operation.Query<Venue> {
    val cols = venueParser.columnNames.filter { it != "id" }.joinToString(", ")
    val values = Fragment.of("").row(venueParser, venue, "id")
    return Sql { "INSERT INTO venue ($cols) VALUES ($values) RETURNING ${venueParser.columnList}" }
        .query(venueParser.exactlyOne())
}

fun analyzeVenueQueries(conn: Connection): List<QueryAnalysis> = listOf(
    QueryAnalyzer.analyze(allVenues.named("allVenues"), conn),
    QueryAnalyzer.analyze(venueById(VenueId(0)).named("venueById"), conn),
    QueryAnalyzer.analyze(createVenue(Venue(VenueId(0), "", Address("", "", "", "", ""), 0, emptyList(), emptyMap())).named("createVenue"), conn),
).flatten()
