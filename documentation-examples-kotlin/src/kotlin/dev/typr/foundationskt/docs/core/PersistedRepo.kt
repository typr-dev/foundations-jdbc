package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import dev.typr.foundationskt.docs.core.PersistedTypes.*

@Suppress("unused")
//start
object VenueRepo {
    private val venueIdType = DuckDbTypes.bigint.transform(::VenueId, VenueId::value)

    private val venueCodec = RowCodec.namedBuilder<Venue>()
        .field("name", DuckDbTypes.varchar, Venue::name)
        .field("capacity", DuckDbTypes.integer, Venue::capacity)
        .build(::Venue)

    private val persistedVenueCodec =
        RowCodec.ofNamed("id", venueIdType)
            .join(venueCodec)
            .to({ (id, venue) -> PersistedVenue(id, venue) }, { Pair(it.id, it.venue) })

    val insert: RowTemplate.Query<Venue, PersistedVenue> =
        Fragment.insertIntoReturning("venue", venueCodec, persistedVenueCodec)

    val selectAll: Operation<List<PersistedVenue>> =
        sql { "SELECT ${persistedVenueCodec.columnList} FROM venue" }
            .query(persistedVenueCodec.all())

    val selectById: Template<VenueId, PersistedVenue?> =
        sql { "SELECT ${persistedVenueCodec.columnList} FROM venue WHERE id = " }
            .param(venueIdType)
            .query(persistedVenueCodec.maxOne())
}
//stop
