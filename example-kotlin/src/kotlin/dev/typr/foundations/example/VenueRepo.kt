package dev.typr.foundations.example

import dev.typr.foundationskt.*

object VenueRepo {
    val allVenues: OperationRead<List<Venue>> =
        sql { "SELECT ${venueCodec.columnList} FROM venue ORDER BY name" }
            .query(venueCodec.all())

    fun venueById(id: VenueId): OperationRead<Venue?> =
        sql { "SELECT ${venueCodec.columnList} FROM venue WHERE id = " }
            .value(venueIdType, id)
            .query(venueCodec.maxOne())

    val createVenue: RowParamBuilder<Venue> =
        Fragment.insertIntoReturning("venue", venueCodec, "id")

    fun createVenue(venue: Venue): OperationRead.Query<Venue> =
        createVenue.updateReturning(venue)
}
