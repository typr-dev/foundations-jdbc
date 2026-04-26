package dev.typr.foundations.example

import dev.typr.foundationskt.*

object EventRepo {
    val allEvents: OperationRead<List<Event>> =
        sql { "SELECT ${eventCodec.columnList} FROM event ORDER BY date" }
            .query(eventCodec.all())

    fun eventById(id: EventId): OperationRead<Event?> =
        sql { "SELECT ${eventCodec.columnList} FROM event WHERE id = " }
            .value(eventIdType, id)
            .query(eventCodec.maxOne())

    fun eventsByStatus(status: EventStatus): OperationRead<List<Event>> =
        sql { "SELECT ${eventCodec.columnList} FROM event WHERE status = " }
            .value(eventStatusType, status)
            .query(eventCodec.all())

    fun eventsByVenue(venueId: VenueId): OperationRead<List<Event>> =
        sql { "SELECT ${eventCodec.columnList} FROM event WHERE venue_id = " }
            .value(venueIdType, venueId)
            .query(eventCodec.all())

    fun createEvent(event: Event): OperationRead.Query<Event> =
        Fragment.insertOneReturning("event", eventCodec, event, "id")

    fun updateEventStatus(status: EventStatus, id: EventId): Operation.Update =
        Fragment.of("UPDATE event SET status = ")
            .value(eventStatusType, status)
            .append(" WHERE id = ")
            .value(eventIdType, id)
            .update()

    fun addEventRating(rating: Double, id: EventId): Operation.Update =
        Fragment.of("UPDATE event SET ratings = list_append(ratings, ")
            .value(DuckDbTypes.double_, rating)
            .append(") WHERE id = ")
            .value(eventIdType, id)
            .update()
}
