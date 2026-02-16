package dev.typr.foundations.example

import dev.typr.foundationskt.DuckDbTypes
import dev.typr.foundationskt.Fragment
import dev.typr.foundationskt.Operation
import dev.typr.foundationskt.QueryAnalysis
import dev.typr.foundationskt.QueryAnalyzer
import dev.typr.foundationskt.Sql
import dev.typr.foundationskt.invoke
import java.sql.Connection

val allEvents: Operation.Query<List<Event>> =
    Sql { "SELECT ${eventParser.columnList} FROM event ORDER BY date" }
        .query(eventParser.all())

fun eventById(id: EventId): Operation.Query<Event?> =
    Sql { "SELECT ${eventParser.columnList} FROM event WHERE id = ${eventIdType(id)}" }
        .query(eventParser.maxOne())

fun eventsByStatus(status: EventStatus): Operation.Query<List<Event>> =
    Sql { "SELECT ${eventParser.columnList} FROM event WHERE status = ${eventStatusType(status)}" }
        .query(eventParser.all())

fun eventsByVenue(venueId: VenueId): Operation.Query<List<Event>> =
    Sql { "SELECT ${eventParser.columnList} FROM event WHERE venue_id = ${venueIdType(venueId)}" }
        .query(eventParser.all())

fun createEvent(event: Event): Operation.Query<Event> {
    val cols = eventParser.columnNames.filter { it != "id" }.joinToString(", ")
    val values = Fragment.of("").row(eventParser, event, "id")
    return Sql { "INSERT INTO event ($cols) VALUES ($values) RETURNING ${eventParser.columnList}" }
        .query(eventParser.exactlyOne())
}

fun updateEventStatus(id: EventId, status: EventStatus): Operation.Update =
    Sql {
        """UPDATE event SET status = ${eventStatusType(status)} 
        WHERE id = ${eventIdType(id)}"""
    }.update()

fun addEventRating(id: EventId, rating: Double): Operation.Update =
    Sql { """UPDATE event 
        SET ratings = list_append(ratings, ${DuckDbTypes.double_(rating)}) 
        WHERE id = ${eventIdType(id)}"""
    }.update()

fun analyzeEventQueries(conn: Connection): List<QueryAnalysis> = listOf(
    QueryAnalyzer.analyze("allEvents", allEvents, conn),
    QueryAnalyzer.analyze("eventById", eventById(EventId(0)), conn),
    QueryAnalyzer.analyze("eventsByStatus", eventsByStatus(EventStatus.DRAFT), conn),
    QueryAnalyzer.analyze("eventsByVenue", eventsByVenue(VenueId(0)), conn),
    QueryAnalyzer.analyze(
        "createEvent",
        createEvent(
            Event(
                EventId(0),
                VenueId(0),
                "",
                null,
                EventStatus.DRAFT,
                java.time.OffsetDateTime.now(),
                java.time.LocalDate.now(),
                Money(java.math.BigDecimal.ZERO),
                emptyList(),
                emptyList()
            )
        ),
        conn
    ),
    QueryAnalyzer.analyze("updateEventStatus", updateEventStatus(EventId(0), EventStatus.DRAFT), conn),
    QueryAnalyzer.analyze("addEventRating", addEventRating(EventId(0), 0.0), conn),
).flatten()
