package dev.typr.foundations.example

import dev.typr.foundationskt.*

object EventRepo {
    val allEvents: Operation<List<Event>> =
        sql { "SELECT ${eventCodec.columnList} FROM event ORDER BY date" }
            .query(eventCodec.all())

    val eventById: Template<EventId, Event?> =
        sql { "SELECT ${eventCodec.columnList} FROM event WHERE id = " }
            .param(eventIdType)
            .query(eventCodec.maxOne())

    val eventsByStatus: Template<EventStatus, List<Event>> =
        sql { "SELECT ${eventCodec.columnList} FROM event WHERE status = " }
            .param(eventStatusType)
            .query(eventCodec.all())

    val eventsByVenue: Template<VenueId, List<Event>> =
        sql { "SELECT ${eventCodec.columnList} FROM event WHERE venue_id = " }
            .param(venueIdType)
            .query(eventCodec.all())

    private val insertCols = eventCodec.columnNames.filter { it != "id" }.joinToString(", ")

    val createEvent: RowTemplate<Event, Event> =
        Fragment.of("INSERT INTO event ($insertCols) VALUES (")
            .paramRow(eventCodec, "id")
            .append(sql { ") RETURNING ${eventCodec.columnList}" })
            .query(eventCodec.exactlyOne())

    val updateEventStatus: Template.Update2<EventStatus, EventId> =
        Fragment.of("UPDATE event SET status = ")
            .param(eventStatusType)
            .append(Fragment.of(" WHERE id = "))
            .param(eventIdType)
            .update()

    val addEventRating: Template.Update2<Double, EventId> =
        Fragment.of("UPDATE event SET ratings = list_append(ratings, ")
            .param(DuckDbTypes.double_)
            .append(Fragment.of(") WHERE id = "))
            .param(eventIdType)
            .update()

    val analyzables: List<Analyzable> = listOf(
        allEvents, eventById, eventsByStatus, eventsByVenue,
        createEvent, updateEventStatus, addEventRating
    )
}
