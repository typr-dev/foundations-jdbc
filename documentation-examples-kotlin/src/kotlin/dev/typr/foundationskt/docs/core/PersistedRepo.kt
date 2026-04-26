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

    fun insert(venue: Venue): OperationRead.Query<PersistedVenue> =
        Fragment.insertOneReturning("venue", venueCodec, venue, persistedVenueCodec)

    val selectAll: OperationRead<List<PersistedVenue>> =
        sql { "SELECT ${persistedVenueCodec.columnList} FROM venue" }
            .query(persistedVenueCodec.all())

    fun selectById(id: VenueId): OperationRead.Query<PersistedVenue?> =
        sql { "SELECT ${persistedVenueCodec.columnList} FROM venue WHERE id = ${venueIdType(id)}" }
            .query(persistedVenueCodec.maxOne())
}
//stop
