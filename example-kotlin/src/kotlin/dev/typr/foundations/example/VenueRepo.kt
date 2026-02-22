package dev.typr.foundations.example

import dev.typr.foundationskt.*

object VenueRepo {
    val allVenues: Operation<List<Venue>> =
        sql { "SELECT ${venueCodec.columnList} FROM venue ORDER BY name" }
            .query(venueCodec.all())

    val venueById: Template<VenueId, Venue?> =
        sql { "SELECT ${venueCodec.columnList} FROM venue WHERE id = " }
            .param(venueIdType)
            .query(venueCodec.maxOne())

    val createVenue: RowTemplate<Venue, Venue> =
        Fragment.insertIntoReturning("venue", venueCodec, "id")

}
